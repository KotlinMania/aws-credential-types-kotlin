// port-lint: source provider/credentials.rs
package io.github.kotlinmania.awscredentialtypes.provider

import io.github.kotlinmania.awscredentialtypes.Credentials
import io.github.kotlinmania.awscredentialtypes.Identity
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideCredentials as ProvideCredentialsFuture

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
 * [`ProvideCredentials`]. Generally, this is best done by
 * defining an inherent `async fn` on your structure, then calling that method directly from
 * the trait implementation.
 *
 * ```kotlin
 * class SubprocessCredentialProvider : ProvideCredentials {
 *     // implementation elided...
 *     suspend fun invokeCommand(command: String): String = "some credentials"
 *
 *     /** Parse access key and secret from the first two lines of a string */
 *     fun parseCredentials(creds: String): Result {
 *         val lines = creds.lines()
 *         val akid = lines.getOrNull(0)
 *             ?: return Result.failure(CredentialsError.providerError("invalid credentials"))
 *         val secret = lines.getOrNull(1)
 *             ?: return Result.failure(CredentialsError.providerError("invalid credentials"))
 *         return Result.success(Credentials.new(akid, secret, null, null, "CustomCommand"))
 *     }
 *
 *     suspend fun loadCredentials(): Result {
 *         val creds = invokeCommand("load-credentials.py")
 *         return parseCredentials(creds)
 *     }
 *
 *     override fun provideCredentials(): ProvideCredentialsFuture =
 *         ProvideCredentialsFuture.new { loadCredentials() }
 * }
 * ```
 */

/** Result type for credential providers. */
typealias Result = kotlin.Result<Credentials>

/** Asynchronous Credentials Provider */
interface ProvideCredentials {
    /** Returns a future that provides credentials. */
    fun provideCredentials(): ProvideCredentialsFuture

    /**
     * Returns fallback credentials.
     *
     * This method should be used as a fallback plan, i.e., when
     * a call to `provide_credentials` is interrupted and its future
     * fails to complete.
     *
     * The fallback credentials should be set aside and ready to be returned
     * immediately. Therefore, the user should NOT go fetch new credentials
     * within this method, which might cause a long-running operation.
     */
    fun fallbackOnInterrupt(): Credentials? = null
}

/**
 * Credentials Provider wrapper that may be shared
 *
 * Newtype wrapper around ProvideCredentials that implements Clone using an internal
 * Arc.
 */
class SharedCredentialsProvider(
    private val provider: ProvideCredentials,
) : ProvideCredentials {
    private val cachePartition = Any()

    /** Returns the underlying provider. */
    fun asRef(): ProvideCredentials = provider

    override fun provideCredentials(): ProvideCredentialsFuture = provider.provideCredentials()

    override fun fallbackOnInterrupt(): Credentials? = provider.fallbackOnInterrupt()

    /** Resolves the identity from this credentials provider, mirroring upstream ResolveIdentity. */
    suspend fun resolveIdentity(): kotlin.Result<Identity> =
        provideCredentials().await().map { it.toIdentity() }

    /** Returns the stable cache partition associated with this provider. */
    fun cachePartition(): Any = cachePartition

    override fun toString(): String = "SharedCredentialsProvider"

    companion object {
        /**
         * Create a new SharedCredentials provider from `ProvideCredentials`
         *
         * The given provider will be wrapped in an internal `Arc`. If your
         * provider is already in an `Arc`, use `SharedCredentialsProvider::from(provider)` instead.
         */
        fun new(provider: ProvideCredentials): SharedCredentialsProvider = SharedCredentialsProvider(provider)

        /** Creates a [SharedCredentialsProvider] from [ProvideCredentials]. */
        fun from(provider: ProvideCredentials): SharedCredentialsProvider = SharedCredentialsProvider(provider)
    }
}

/** Storer type for [SharedCredentialsProvider] mirroring upstream `Storable::Storer = StoreReplace<SharedCredentialsProvider>`. */
typealias Storer = SharedCredentialsProvider
