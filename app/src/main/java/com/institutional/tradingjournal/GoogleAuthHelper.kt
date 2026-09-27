package com.institutional.tradingjournal

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Base64
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.SecureRandom

object GoogleAuthHelper {

    private fun findActivity(context: Context): Activity? {
        var currentContext = context

        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) {
                return currentContext
            }

            currentContext = currentContext.baseContext
        }

        return null
    }

    private fun generateSecureRandomNonce(): String {
        val randomBytes = ByteArray(32)

        SecureRandom().nextBytes(randomBytes)

        return Base64.encodeToString(
            randomBytes,
            Base64.NO_WRAP or
                Base64.URL_SAFE or
                Base64.NO_PADDING
        )
    }

    fun signInWithGoogle(
        context: Context,
        webClientId: String,
        onResult: (Boolean, String?, String?) -> Unit
    ) {

        val activity = findActivity(context)

        if (activity == null) {
            onResult(
                false,
                null,
                "Google Sign-In requires an Activity context."
            )
            return
        }

        val credentialManager =
            CredentialManager.create(activity)

        val signInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(
                serverClientId = webClientId
            )
                .setNonce(
                    generateSecureRandomNonce()
                )
                .build()

        /*
         * Explicit "Continue with Google" button flow.
         *
         * Google requires this request to contain
         * exactly one GetSignInWithGoogleOption.
         */
        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(
                    signInWithGoogleOption
                )
                .build()

        CoroutineScope(
            Dispatchers.Main
        ).launch {

            try {

                val result =
                    credentialManager.getCredential(
                        context = activity,
                        request = request
                    )

                val credential =
                    result.credential

                if (
                    credential.type !=
                    GoogleIdTokenCredential
                        .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
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

            } catch (e: GetCredentialException) {

                onResult(
                    false,
                    null,
                    e.localizedMessage
                        ?: "Google Sign-In was cancelled or failed."
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
