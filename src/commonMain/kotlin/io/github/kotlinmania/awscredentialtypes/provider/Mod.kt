// port-lint: source src/provider.rs
package io.github.kotlinmania.awscredentialtypes.provider

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
//
// Tracking ledger for upstream re-exports out of `src/provider.rs`:
//
//   pub use credentials::{ProvideCredentials, Result, SharedCredentialsProvider};
//
// Defining Kotlin locations (no typealias bridge per repo policy):
//   * [ProvideCredentials]        → io.github.kotlinmania.awscredentialtypes.provider.ProvideCredentials
//   * [SharedCredentialsProvider] → io.github.kotlinmania.awscredentialtypes.provider.SharedCredentialsProvider
//   * Result                      → kotlin.Result<Credentials>
//
// Callers migrated:
//   (none yet — codex-kotlin has not been wired up at the time of this port)
