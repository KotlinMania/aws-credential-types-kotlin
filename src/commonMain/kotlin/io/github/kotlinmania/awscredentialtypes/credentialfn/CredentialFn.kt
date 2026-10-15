// port-lint: source credential_fn.rs
package io.github.kotlinmania.awscredentialtypes.credentialfn

import io.github.kotlinmania.awscredentialtypes.Credentials
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
 * See [provideCredentialsFn] for more details (in `aws_credential_types::credential_fn`).
 */
class ProvideCredentialsFn internal constructor(
    private val f: suspend () -> Result<Credentials>,
) : ProvideCredentials {
    override fun provideCredentials(): ProvideCredentialsFuture =
        ProvideCredentialsFuture.new { f() }

    override fun toString(): String = "ProvideCredentialsFn"

    /** Formats the provider for debugging, mirroring upstream Debug::fmt. */
    fun fmt(): String = toString()
}

/**
 * Returns a new credentials provider built with the given closure. This allows you
 * to create an [ProvideCredentials] implementation from an async block that returns
 * a result (matching `crate::provider::Result`).
 *
 * # Examples
 *
 * ```kotlin
 * // use aws_credential_types::Credentials
 * // use aws_credential_types::credential_fn::provide_credentials_fn
 * suspend fun loadCredentials(): Credentials {
 *     return Credentials.new("akid", "secret", null, null, "example")
 * }
 *
 * provideCredentialsFn {
 *     // Async process to retrieve credentials goes here
 *     val credentials = loadCredentials()
 *     Result.success(credentials)
 * }
 * ```
 */
fun provideCredentialsFn(f: suspend () -> Result<Credentials>): ProvideCredentialsFn =
    ProvideCredentialsFn(f)
