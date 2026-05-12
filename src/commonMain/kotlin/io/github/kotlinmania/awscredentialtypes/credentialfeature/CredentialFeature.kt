// port-lint: source src/credential_feature.rs
package io.github.kotlinmania.awscredentialtypes.credentialfeature

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/** IDs for the credential related features that may be used in the AWS SDK */
enum class AwsCredentialFeature {
    /** An operation where credential resolution resolved an account ID */
    RESOLVED_ACCOUNT_ID,

    /** An operation called using credentials resolved from code, cli parameters, session object, or client instance */
    CREDENTIALS_CODE,

    /** An operation called using credentials resolved from environment variables */
    CREDENTIALS_ENV_VARS,

    /** An operation called using credentials resolved from environment variables for assuming a role with STS using a web identity token */
    CREDENTIALS_ENV_VARS_STS_WEB_ID_TOKEN,

    /** An operation called using credentials resolved from STS using assume role */
    CREDENTIALS_STS_ASSUME_ROLE,

    /** An operation called using credentials resolved from STS using assume role with SAML */
    CREDENTIALS_STS_ASSUME_ROLE_SAML,

    /** An operation called using credentials resolved from STS using assume role with web identity */
    CREDENTIALS_STS_ASSUME_ROLE_WEB_ID,

    /** An operation called using credentials resolved from STS using a federation token */
    CREDENTIALS_STS_FEDERATION_TOKEN,

    /** An operation called using credentials resolved from STS using a session token */
    CREDENTIALS_STS_SESSION_TOKEN,

    /** An operation called using credentials resolved from a config file(s) profile with static credentials */
    CREDENTIALS_PROFILE,

    /** An operation called using credentials resolved from a source profile in a config file(s) profile */
    CREDENTIALS_PROFILE_SOURCE_PROFILE,

    /** An operation called using credentials resolved from a named provider in a config file(s) profile */
    CREDENTIALS_PROFILE_NAMED_PROVIDER,

    /** An operation called using credentials resolved from configuration for assuming a role with STS using web identity token in a config file(s) profile */
    CREDENTIALS_PROFILE_STS_WEB_ID_TOKEN,

    /** An operation called using credentials resolved from an SSO session in a config file(s) profile */
    CREDENTIALS_PROFILE_SSO,

    /** An operation called using credentials resolved from an SSO session */
    CREDENTIALS_SSO,

    /** An operation called using credentials resolved from a process in a config file(s) profile */
    CREDENTIALS_PROFILE_PROCESS,

    /** An operation called using credentials resolved from a process */
    CREDENTIALS_PROCESS,

    /** An operation called using credentials resolved from an HTTP endpoint */
    CREDENTIALS_HTTP,

    /** An operation called using credentials resolved from the instance metadata service (IMDS) */
    CREDENTIALS_IMDS,

    /** An operation called using a Bearer token resolved from service-specific environment variables */
    BEARER_SERVICE_ENV_VARS,

    /** An operation called using S3 Express bucket credentials */
    S3_EXPRESS_BUCKET,

    /** An operation called using credentials resolved from a LoginCredentialsProvider configured via profile */
    CREDENTIALS_PROFILE_LOGIN,

    /** An operation called using credentials resolved from a LoginCredentialsProvider configured explicitly via code */
    CREDENTIALS_LOGIN,
}
