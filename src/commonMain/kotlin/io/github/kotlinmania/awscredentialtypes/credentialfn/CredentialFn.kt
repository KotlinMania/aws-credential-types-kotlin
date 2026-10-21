// port-lint: source credential_fn.rs
package io.github.kotlinmania.awscredentialtypes.credentialfn

import io.github.kotlinmania.awscredentialtypes.provider.CredentialsResult
import io.github.kotlinmania.awscredentialtypes.provider.ProvideCredentials
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideCredentials as ProvideCredentialsFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Types that allow a credentials provider to be created from a closure
 */

/**
 * A [ProvideCredentials] implemented by a closure.
 *
 * See [provideCredentialsFn].
 */
class ProvideCredentialsFn internal constructor(
    private val f: suspend () -> CredentialsResult,
) : ProvideCredentials {
    override fun provideCredentials(): ProvideCredentialsFuture =
        ProvideCredentialsFuture.new { f() }

    override fun toString(): String = "ProvideCredentialsFn"

    /** Formats the provider for debugging. */
    fun fmt(): String = toString()
}

/**
 * Returns a new credentials provider built with the given closure. This allows you
 * to create an [ProvideCredentials] implementation from an async block that returns
 * a [CredentialsResult].
 */
fun provideCredentialsFn(f: suspend () -> CredentialsResult): ProvideCredentialsFn =
    ProvideCredentialsFn(f)
