// port-lint: source provider/credentials.rs
package io.github.kotlinmania.awscredentialtypes.provider

import io.github.kotlinmania.awscredentialtypes.Credentials
import io.github.kotlinmania.awscredentialtypes.Identity
import io.github.kotlinmania.awscredentialtypes.provider.error.CredentialsError
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideCredentials as ProvideCredentialsFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/** A credentials value or the original provider error. */
class CredentialsResult private constructor(
    val value: Credentials?,
    val error: CredentialsError?,
) {
    val isSuccess: Boolean
        get() = error == null

    val isFailure: Boolean
        get() = error != null

    fun getOrNull(): Credentials? = value

    fun exceptionOrNull(): CredentialsError? = error

    @Throws(CredentialsError::class)
    fun getOrThrow(): Credentials {
        val failure = error
        if (failure != null) throw failure
        return requireNotNull(value)
    }

    companion object {
        fun success(value: Credentials): CredentialsResult = CredentialsResult(value, null)

        fun failure(error: CredentialsError): CredentialsResult = CredentialsResult(null, error)
    }
}

/** An identity value or the original provider error. */
class CredentialsIdentityResult private constructor(
    val value: Identity?,
    val error: CredentialsError?,
) {
    val isSuccess: Boolean
        get() = error == null

    val isFailure: Boolean
        get() = error != null

    fun getOrNull(): Identity? = value

    fun exceptionOrNull(): CredentialsError? = error

    @Throws(CredentialsError::class)
    fun getOrThrow(): Identity {
        val failure = error
        if (failure != null) throw failure
        return requireNotNull(value)
    }

    companion object {
        fun success(value: Identity): CredentialsIdentityResult = CredentialsIdentityResult(value, null)

        fun failure(error: CredentialsError): CredentialsIdentityResult = CredentialsIdentityResult(null, error)
    }
}

/** Asynchronous Credentials Provider */
interface ProvideCredentials {
    /** Returns a future that provides credentials. */
    fun provideCredentials(): ProvideCredentialsFuture

    /**
     * Returns fallback credentials.
     *
     * This method should be used as a fallback plan, i.e., when
     * a call to [provideCredentials] is interrupted and its future
     * fails to complete.
     *
     * The fallback credentials should be set aside and ready to be returned
     * immediately. Therefore, the user should NOT go fetch new credentials
     * within this method, which might cause a long-running operation.
     */
    fun fallbackOnInterrupt(): Credentials? = null
}

/** Credentials provider wrapper that shares its underlying provider. */
class SharedCredentialsProvider(
    private val provider: ProvideCredentials,
) : ProvideCredentials {
    private val cachePartition = Any()

    /** Returns the underlying provider. */
    fun asRef(): ProvideCredentials = provider

    override fun provideCredentials(): ProvideCredentialsFuture = provider.provideCredentials()

    override fun fallbackOnInterrupt(): Credentials? = provider.fallbackOnInterrupt()

    /** Resolves the identity from this credentials provider. */
    suspend fun resolveIdentity(): CredentialsIdentityResult {
        val result = provideCredentials().await()
        val failure = result.error
        return if (failure != null) {
            CredentialsIdentityResult.failure(failure)
        } else {
            CredentialsIdentityResult.success(result.getOrThrow().toIdentity())
        }
    }

    /** Returns the stable cache partition associated with this provider. */
    fun cachePartition(): Any = cachePartition

    override fun toString(): String = "SharedCredentialsProvider"

    companion object {
        /** Creates a shared wrapper around the given provider. */
        fun new(provider: ProvideCredentials): SharedCredentialsProvider = SharedCredentialsProvider(provider)

        /** Creates a [SharedCredentialsProvider] from [ProvideCredentials]. */
        fun from(provider: ProvideCredentials): SharedCredentialsProvider = SharedCredentialsProvider(provider)
    }
}
