// port-lint: source src/provider/credentials.rs
package io.github.kotlinmania.awscredentialtypes.provider

import io.github.kotlinmania.awscredentialtypes.Credentials
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideCredentials as ProvideCredentialsFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// AWS SDK Credentials
//
// ## Implementing your own credentials provider
//
// While for many use cases, using a built in credentials provider is sufficient, you may want to
// implement your own credential provider.
//
// ### With static credentials
//
// _Note: In general, you should prefer to use the credential providers that come
// with the AWS SDK to get credentials. It is __NOT__ secure to hardcode credentials
// into your application. Only use this approach if you really know what you're doing._
//
// See [Credentials.fromKeys] for an example on how to use static credentials.
//
// ### With dynamically loaded credentials
//
// If you are loading credentials dynamically, you can provide your own implementation of
// [ProvideCredentials]. Generally, this is best done by defining an inherent suspend method on
// your structure, then calling that method directly from the interface implementation.

/** Asynchronous Credentials Provider */
interface ProvideCredentials {
    /** Returns a holder that yields credentials. */
    fun provideCredentials(): ProvideCredentialsFuture

    /**
     * Returns fallback credentials.
     *
     * This method should be used as a fallback plan, i.e., when
     * a call to [provideCredentials] is interrupted and fails to complete.
     *
     * The fallback credentials should be set aside and ready to be returned
     * immediately. Therefore, the user should NOT go fetch new credentials
     * within this method, which might cause a long-running operation.
     */
    fun fallbackOnInterrupt(): Credentials? = null
}

/**
 * Credentials Provider wrapper that may be shared.
 *
 * Newtype wrapper around [ProvideCredentials] that forwards calls to an inner provider.
 */
class SharedCredentialsProvider(
    private val provider: ProvideCredentials,
) : ProvideCredentials {
    /** Returns the underlying provider. */
    fun asRef(): ProvideCredentials = provider

    override fun provideCredentials(): ProvideCredentialsFuture = provider.provideCredentials()

    override fun fallbackOnInterrupt(): Credentials? = provider.fallbackOnInterrupt()

    override fun toString(): String = "SharedCredentialsProvider"
}
