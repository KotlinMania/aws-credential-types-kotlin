// port-lint: source provider/token.rs
package io.github.kotlinmania.awscredentialtypes.provider.token

import io.github.kotlinmania.awscredentialtypes.Token
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideToken as ProvideTokenFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// AWS Access Tokens for SSO
//
// When authenticating with an AWS Builder ID, single sign-on (SSO) will provide an access token
// that can then be used to authenticate with services such as CodeCatalyst.
//
// This module provides the [ProvideToken] interface that is used to configure token providers in
// the SDK config.

/** Result type for token providers. */
typealias Result = kotlin.Result<Token>

/** Access Token Provider */
interface ProvideToken {
    /** Returns a holder that provides an access token. */
    fun provideToken(): ProvideTokenFuture
}

/**
 * Access token provider wrapper that may be shared.
 *
 * Newtype wrapper around [ProvideToken] that forwards calls to an inner provider.
 */
class SharedTokenProvider(
    private val provider: ProvideToken,
) : ProvideToken {
    private val cachePartition = Any()

    /** Returns the underlying provider. */
    fun asRef(): ProvideToken = provider

    override fun provideToken(): ProvideTokenFuture = provider.provideToken()

    /** Returns the stable cache partition associated with this provider. */
    fun cachePartition(): Any = cachePartition

    override fun toString(): String = "SharedTokenProvider"

    companion object {
        /** Create a new [SharedTokenProvider] from [ProvideToken]. */
        fun new(provider: ProvideToken): SharedTokenProvider = SharedTokenProvider(provider)
    }
}
