package com.example.ecocafeconnect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "AuthViewModel"

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d(TAG, "User not authenticated")
            _authState.value = AuthState.Unauthenticated
        } else {
            Log.d(TAG, "User authenticated: ${currentUser.email}")
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Login successful: ${auth.currentUser?.email}")
                    _authState.value = AuthState.Authenticated
                } else {
                    Log.e(TAG, "Login failed: ${task.exception?.message}")
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Signup successful: ${auth.currentUser?.email}")
                    _authState.value = AuthState.Authenticated
                } else {
                    Log.e(TAG, "Signup failed: ${task.exception?.message}")
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signInWithGoogle(idToken: String) {
        Log.d(TAG, "signInWithGoogle called")
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()

                if (authResult.user != null) {
                    Log.d(TAG, "Google sign in successful! User: ${authResult.user?.email}")
                    Log.d(TAG, "User UID: ${authResult.user?.uid}")
                    _authState.value = AuthState.Authenticated
                } else {
                    Log.e(TAG, "Google sign in failed: User is null")
                    _authState.value = AuthState.Error("Failed to sign in with Google")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google sign in failed: ${e.message}", e)
                _authState.value = AuthState.Error(e.message ?: "Google sign in failed")
            }
        }
    }

    fun signout() {
        auth.signOut()
        Log.d(TAG, "User signed out")
        _authState.value = AuthState.Unauthenticated
    }
}