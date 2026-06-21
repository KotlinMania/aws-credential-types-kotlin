// port-lint: source token_fn.rs
package io.github.kotlinmania.awscredentialtypes.tokenfn

import io.github.kotlinmania.awscredentialtypes.provider.token.ProvideToken
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideToken as ProvideTokenFuture
import io.github.kotlinmania.awscredentialtypes.provider.token.Result as TokenResult

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// Types that allow an access token provider to be created from a closure.

/**
 * A [ProvideToken] implemented by a closure.
 *
 * See [provideTokenFn] for more details.
 */
class ProvideTokenFn internal constructor(
    private val provider: suspend () -> TokenResult,
) : ProvideToken {
    override fun provideToken(): ProvideTokenFuture =
        ProvideTokenFuture.new { provider() }

    override fun toString(): String = "ProvideTokenFn"
}

/**
 * Returns a new token provider built with the given closure.
 *
 * This allows you to create a [ProvideToken] implementation from a suspending block that returns a
 * token-provider result.
 */
fun provideTokenFn(provider: suspend () -> TokenResult): ProvideTokenFn =
    ProvideTokenFn(provider)
