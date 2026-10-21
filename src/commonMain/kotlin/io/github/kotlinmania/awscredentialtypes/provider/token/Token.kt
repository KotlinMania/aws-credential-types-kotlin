// port-lint: source provider/token.rs
package io.github.kotlinmania.awscredentialtypes.provider.token

import io.github.kotlinmania.awscredentialtypes.Identity
import io.github.kotlinmania.awscredentialtypes.Token
import io.github.kotlinmania.awscredentialtypes.provider.error.TokenError
import io.github.kotlinmania.awscredentialtypes.provider.error.TokenException
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideToken as ProvideTokenFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * AWS Access Tokens for SSO
 *
 * When authenticating with an AWS Builder ID, single sign-on (SSO) will provide
 * an access token that can then be used to authenticate with services such as
 * Code Catalyst.
 *
 * This module provides the [ProvideToken] trait that is used to configure
 * token providers in the SDK config.
 */

/** A token value or the original provider error. */
class TokenResult private constructor(
    val value: Token?,
    val error: TokenError?,
) {
    val isSuccess: Boolean
        get() = error == null

    val isFailure: Boolean
        get() = error != null

    fun getOrNull(): Token? = value

    fun exceptionOrNull(): TokenError? = error

    fun getOrThrow(): Token {
        val failure = error
        if (failure != null) throw TokenException(failure)
        return requireNotNull(value)
    }

    companion object {
        fun success(value: Token): TokenResult = TokenResult(value, null)

        fun failure(error: TokenError): TokenResult = TokenResult(null, error)
    }
}

/** An identity value or the original provider error. */
class TokenIdentityResult private constructor(
    val value: Identity?,
    val error: TokenError?,
) {
    val isSuccess: Boolean
        get() = error == null

    val isFailure: Boolean
        get() = error != null

    fun getOrNull(): Identity? = value

    fun exceptionOrNull(): TokenError? = error

    fun getOrThrow(): Identity {
        val failure = error
        if (failure != null) throw TokenException(failure)
        return requireNotNull(value)
    }

    companion object {
        fun success(value: Identity): TokenIdentityResult = TokenIdentityResult(value, null)

        fun failure(error: TokenError): TokenIdentityResult = TokenIdentityResult(null, error)
    }
}

/** Access Token Provider */
interface ProvideToken {
    /** Returns a future that provides an access token. */
    fun provideToken(): ProvideTokenFuture
}

/** Access token provider wrapper that shares its underlying provider. */
class SharedTokenProvider(
    private val provider: ProvideToken,
) : ProvideToken {
    private val cachePartition = Any()

    /** Returns the underlying provider. */
    fun asRef(): ProvideToken = provider

    override fun provideToken(): ProvideTokenFuture = provider.provideToken()

    /** Resolves the identity from this token provider. */
    suspend fun resolveIdentity(): TokenIdentityResult {
        val result = provideToken().await()
        val failure = result.error
        return if (failure != null) {
            TokenIdentityResult.failure(failure)
        } else {
            TokenIdentityResult.success(result.getOrThrow().toIdentity())
        }
    }

    /** Returns the stable cache partition associated with this provider. */
    fun cachePartition(): Any = cachePartition

    override fun toString(): String = "SharedTokenProvider"

    companion object {
        /** Creates a shared wrapper around the given provider. */
        fun new(provider: ProvideToken): SharedTokenProvider = SharedTokenProvider(provider)

        /** Creates a [SharedTokenProvider] from [ProvideToken]. */
        fun from(provider: ProvideToken): SharedTokenProvider = SharedTokenProvider(provider)
    }
}
