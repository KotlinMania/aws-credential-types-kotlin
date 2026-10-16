// port-lint: source provider.rs
package io.github.kotlinmania.awscredentialtypes.provider

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * AWS SDK Credentials
 *
 * ## Implementing your own credentials provider
 *
 * While for many use cases, using a built in credentials provider is sufficient, you may want to
 * implement your own credential provider.
 *
 * ### With static credentials
 *
 * _Note: In general, you should prefer to use the credential providers that come
 * with the AWS SDK to get credentials. It is __NOT__ secure to hardcode credentials
 * into your application. Only use this approach if you really know what you're doing._
 *
 * See [`Credentials::from_keys`](crate::Credentials::from_keys) for an example on how to use static credentials.
 * Enable the `hardcoded-credentials` feature to be able to use `Credentials::from_keys` to
 * construct credentials from hardcoded values.
 *
 * ### With dynamically loaded credentials
 * If you are loading credentials dynamically, you can provide your own implementation of
 * [ProvideCredentials]. Generally, this is best done by
 * defining an inherent `async fn` on your structure, then calling that method directly from
 * the trait implementation.
 *
 * ```kotlin
 * class SubprocessCredentialProvider : ProvideCredentials {
 *     // implementation elided...
 *     suspend fun invokeCommand(command: String): String {
 *         return "some credentials"
 *     }
 *
 *     /** Parse access key and secret from the first two lines of a string */
 *     fun parseCredentials(creds: String): Result<Credentials> {
 *         val lines = creds.lines()
 *         val akid = lines.getOrNull(0)
 *             ?: return Result.failure(CredentialsError.providerError("invalid credentials"))
 *         val secret = lines.getOrNull(1)
 *             ?: return Result.failure(CredentialsError.providerError("invalid credentials"))
 *         return Result.success(Credentials.new(akid, secret, null, null, "CustomCommand"))
 *     }
 *
 *     suspend fun loadCredentials(): Result<Credentials> {
 *         val creds = invokeCommand("load-credentials.py")
 *         return parseCredentials(creds)
 *     }
 *
 *     override fun provideCredentials(): ProvideCredentialsFuture {
 *         return ProvideCredentialsFuture.new(loadCredentials())
 *     }
 * }
 * ```
 *
 * (Translates upstream `SubprocessCredentialProvider` with `invoke_command`, `parse_credentials`, `load_credentials`, `provide_credentials`, and `provider_error` from `aws_credential_types`.)
 *
 * Re-exports [ProvideCredentials], [Result], and [SharedCredentialsProvider] from credentials.
 */
internal object Mod
