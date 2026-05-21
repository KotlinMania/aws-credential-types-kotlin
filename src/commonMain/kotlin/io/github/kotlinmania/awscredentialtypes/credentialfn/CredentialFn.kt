// port-lint: source credential_fn.rs
package io.github.kotlinmania.awscredentialtypes.credentialfn

import io.github.kotlinmania.awscredentialtypes.Credentials
import io.github.kotlinmania.awscredentialtypes.provider.ProvideCredentials
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideCredentials as ProvideCredentialsFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// Types that allow a credentials provider to be created from a closure.

/**
 * A [ProvideCredentials] implemented by a closure.
 *
 * See [provideCredentialsFn] for more details.
 */
class ProvideCredentialsFn internal constructor(
    private val provider: suspend () -> Result<Credentials>,
) : ProvideCredentials {
    override fun provideCredentials(): ProvideCredentialsFuture =
        ProvideCredentialsFuture.new { provider() }

    override fun toString(): String = "ProvideCredentialsFn"
}

/**
 * Returns a new credentials provider built with the given closure.
 *
 * This allows you to create a [ProvideCredentials] implementation from a suspending
 * block that returns a credential result.
 */
fun provideCredentialsFn(provider: suspend () -> Result<Credentials>): ProvideCredentialsFn =
    ProvideCredentialsFn(provider)
