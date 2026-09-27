package com.institutional.tradingjournal

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object GoogleAuthHelper {

    fun signInWithGoogle(
        context: Context,
        webClientId: String,
        onResult: (Boolean, String?, String?) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        kotlinx.coroutines.CoroutineScope(
            kotlinx.coroutines.Dispatchers.Main
        ).launch {
            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val credential = result.credential

                if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    onResult(
                        false,
                        null,
                        "Invalid Google credential type."
                    )
                    return@launch
                }

                val googleCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                val idToken = googleCredential.idToken

                if (idToken.isBlank()) {
                    onResult(
                        false,
                        null,
                        "Google ID token was empty."
                    )
                    return@launch
                }

                onResult(
                    true,
                    idToken,
                    null
                )

            } catch (e: Exception) {
                onResult(
                    false,
                    null,
                    e.localizedMessage ?: "Google Sign-In failed."
                )
            }
        }
    }
}
