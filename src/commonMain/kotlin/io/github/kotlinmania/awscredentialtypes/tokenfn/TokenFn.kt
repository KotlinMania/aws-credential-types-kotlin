// port-lint: source token_fn.rs
package io.github.kotlinmania.awscredentialtypes.tokenfn

import io.github.kotlinmania.awscredentialtypes.provider.token.ProvideToken
import io.github.kotlinmania.awscredentialtypes.provider.token.TokenResult
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideToken as ProvideTokenFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Types that allow an access token provider to be created from a closure
 */

/**
 * A [ProvideToken] implemented by a closure.
 *
 * See [provideTokenFn].
 */
class ProvideTokenFn internal constructor(
    private val f: suspend () -> TokenResult,
) : ProvideToken {
    override fun provideToken(): ProvideTokenFuture =
        ProvideTokenFuture.new { f() }

    override fun toString(): String = "ProvideTokenFn"

    /** Formats the provider for debugging. */
    fun fmt(): String = toString()
}

/**
 * Returns a new token provider built with the given closure. This allows you
 * to create an [ProvideToken] implementation from an async block that returns
 * a [TokenResult].
 */
fun provideTokenFn(f: suspend () -> TokenResult): ProvideTokenFn =
    ProvideTokenFn(f)
