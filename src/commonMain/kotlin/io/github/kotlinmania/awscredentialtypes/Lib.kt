// port-lint: source src/lib.rs
package io.github.kotlinmania.awscredentialtypes

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// `aws-credential-types` provides types concerned with AWS SDK credentials including:
// * Interfaces for credentials providers and for credentials caching
// * An opaque class representing credentials
// * Concrete implementations of credentials caching
//
// Tracking ledger for upstream `src/lib.rs` module declarations:
//
//   pub mod attributes;          → io.github.kotlinmania.awscredentialtypes.attributes
//   pub mod credential_feature;  → io.github.kotlinmania.awscredentialtypes.credentialfeature
//   pub mod credential_fn;       → not yet ported (depends on async closure support; deferred)
//   mod credentials_impl;        → flattened into io.github.kotlinmania.awscredentialtypes
//   pub mod provider;            → io.github.kotlinmania.awscredentialtypes.provider
//   pub mod token_fn;            → not yet ported (depends on Token)
//
// Tracking ledger for upstream `src/lib.rs` re-exports:
//
//   pub use credentials_impl::{Credentials, CredentialsBuilder};
//
// Defining Kotlin locations (no typealias bridge per repo policy):
//   * [Credentials]        → io.github.kotlinmania.awscredentialtypes.Credentials
//   * [CredentialsBuilder] → io.github.kotlinmania.awscredentialtypes.CredentialsBuilder
//
//   pub type Token = aws_smithy_runtime_api::client::identity::http::Token;
//
// Not yet ported: depends on `aws-smithy-runtime-api`, which currently has no Kotlin counterpart
// in this workspace. Token, ProvideToken, SharedTokenProvider, and token_fn will be wired up
// when `aws-smithy-runtime-api-kotlin` lands.
//
// Callers migrated:
//   (none yet — codex-kotlin has not been wired up at the time of this port)
