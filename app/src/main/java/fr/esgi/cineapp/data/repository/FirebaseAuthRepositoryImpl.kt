package fr.esgi.cineapp.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import fr.esgi.cineapp.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseAuthRepository"
private const val USERS_COLLECTION = "users"

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    private val users get() = firestore.collection(USERS_COLLECTION)

    override suspend fun login(email: String, password: String): Result<User> = suspendRunCatching {
        val firebaseUser = checkNotNull(auth.signInWithEmailAndPassword(email, password).await().user) {
            "Connexion échouée : utilisateur introuvable"
        }

        users.document(firebaseUser.uid).get().await().toObject(User::class.java)
            ?: firebaseUser.toUser(defaultName = email.substringBefore("@")).also { created ->
                // Compte Auth sans profil Firestore (ex. register interrompu entre les deux écritures) : on le recrée.
                users.document(created.id).set(created).await()
            }
    }

    override suspend fun register(username: String, email: String, password: String): Result<User> =
        suspendRunCatching {
            val firebaseUser = checkNotNull(auth.createUserWithEmailAndPassword(email, password).await().user) {
                "Inscription échouée : utilisateur non créé"
            }

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            User(id = firebaseUser.uid, username = username, email = email).also { created ->
                users.document(created.id).set(created).await()
            }
        }

    override suspend fun logout() {
        auth.signOut()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentUser(): Flow<User?> =
        authState().flatMapLatest { firebaseUser ->
            if (firebaseUser == null) flowOf(null) else userProfile(firebaseUser)
        }

    private fun authState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private fun userProfile(firebaseUser: FirebaseUser): Flow<User> = callbackFlow {
        val registration = users.document(firebaseUser.uid).addSnapshotListener { snapshot, error ->
            if (error != null) Log.w(TAG, "Écoute du profil utilisateur en erreur", error)
            trySend(snapshot?.toObject(User::class.java) ?: firebaseUser.toUser())
        }
        awaitClose { registration.remove() }
    }

    private fun FirebaseUser.toUser(defaultName: String = "") = User(
        id = uid,
        email = email.orEmpty(),
        username = displayName ?: defaultName
    )
}

private inline fun <T> suspendRunCatching(block: () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }