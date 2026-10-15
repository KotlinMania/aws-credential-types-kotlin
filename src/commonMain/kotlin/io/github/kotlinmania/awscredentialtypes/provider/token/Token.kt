// port-lint: source provider/token.rs
package io.github.kotlinmania.awscredentialtypes.provider.token

import io.github.kotlinmania.awscredentialtypes.Identity
import io.github.kotlinmania.awscredentialtypes.Token
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

/** Result type for token providers */
typealias Result = kotlin.Result<Token>

/** Access Token Provider */
interface ProvideToken {
    /** Returns a future that provides an access token. */
    fun provideToken(): ProvideTokenFuture
}

/**
 * Access token provider wrapper that may be shared.
 *
 * Newtype wrapper around [ProvideToken] that implements `Clone` using an internal `Arc`.
 */
class SharedTokenProvider(
    private val provider: ProvideToken,
) : ProvideToken {
    private val cachePartition = Any()

    /** Returns the underlying provider. */
    fun asRef(): ProvideToken = provider

    override fun provideToken(): ProvideTokenFuture = provider.provideToken()

    /** Resolves the identity from this token provider, mirroring upstream ResolveIdentity. */
    suspend fun resolveIdentity(): kotlin.Result<Identity> =
        provideToken().await().map { it.toIdentity() }

    /** Returns the stable cache partition associated with this provider. */
    fun cachePartition(): Any = cachePartition

    override fun toString(): String = "SharedTokenProvider"

    companion object {
        /**
         * Create a new [SharedTokenProvider] from [ProvideToken].
         *
         * The given provider will be wrapped in an internal `Arc`. If your
         * provider is already in an `Arc`, use `SharedTokenProvider::from(provider)` instead.
         */
        fun new(provider: ProvideToken): SharedTokenProvider = SharedTokenProvider(provider)

        /** Creates a [SharedTokenProvider] from [ProvideToken]. */
        fun from(provider: ProvideToken): SharedTokenProvider = SharedTokenProvider(provider)
    }
}
