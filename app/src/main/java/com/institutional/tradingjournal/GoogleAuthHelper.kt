package com.institutional.tradingjournal

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.SecureRandom
import android.util.Base64

object GoogleAuthHelper {

    private fun generateSecureRandomNonce(): String {
        val randomBytes = ByteArray(32)
        SecureRandom().nextBytes(randomBytes)

        return Base64.encodeToString(
            randomBytes,
            Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING
        )
    }

    fun signInWithGoogle(
        context: Context,
        webClientId: String,
        onResult: (Boolean, String?, String?) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)

        /*
         * IMPORTANT:
         * This is the explicit "Continue with Google" button flow.
         * Google recommends GetSignInWithGoogleOption for this flow.
         */
        val signInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(
                serverClientId = webClientId
            )
                .setNonce(generateSecureRandomNonce())
                .build()

        /*
         * The Google button flow must contain exactly
         * one GetSignInWithGoogleOption.
         */
        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

        CoroutineScope(Dispatchers.Main).launch {

            try {

                val result =
                    credentialManager.getCredential(
                        context = context,
                        request = request
                    )

                val credential = result.credential

                if (
                    credential.type !=
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    onResult(
                        false,
                        null,
                        "Invalid Google credential type."
                    )

                    return@launch
                }

                val googleCredential =
                    GoogleIdTokenCredential.createFrom(
                        credential.data
                    )

                val idToken =
                    googleCredential.idToken

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
                    e.localizedMessage
                        ?: "Google Sign-In failed."
                )
            }
        }
    }
}
