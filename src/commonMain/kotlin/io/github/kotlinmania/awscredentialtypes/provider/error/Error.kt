// port-lint: source src/provider/error.rs
package io.github.kotlinmania.awscredentialtypes.provider.error

import kotlin.time.Duration

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// Credentials provider errors

/** Details for [CredentialsError.CredentialsNotLoaded] */
class CredentialsNotLoadedDetails internal constructor(
    internal val source: Throwable?,
) {
    override fun toString(): String = "CredentialsNotLoaded(source=$source)"
}

/** Details for [CredentialsError.ProviderTimedOut] or [TokenError.ProviderTimedOut] */
class ProviderTimedOutDetails internal constructor(
    private val timeoutDuration: Duration,
) {
    /** Returns the maximum allowed timeout duration that was exceeded */
    fun timeoutDuration(): Duration = timeoutDuration

    override fun toString(): String = "ProviderTimedOut(timeoutDuration=$timeoutDuration)"
}

/** Details for [CredentialsError.InvalidConfiguration] or [TokenError.InvalidConfiguration] */
class InvalidConfigurationDetails internal constructor(
    internal val source: Throwable,
) {
    override fun toString(): String = "InvalidConfiguration(source=$source)"
}

/** Details for [CredentialsError.ProviderError] or [TokenError.ProviderError] */
class ProviderErrorDetails internal constructor(
    internal val source: Throwable,
) {
    override fun toString(): String = "ProviderError(source=$source)"
}

/** Details for [CredentialsError.Unhandled] or [TokenError.Unhandled] */
class UnhandledDetails internal constructor(
    internal val source: Throwable,
) {
    override fun toString(): String = "Unhandled(source=$source)"
}

/** Error returned when credentials failed to load. */
sealed class CredentialsError(
    message: String,
    cause: Throwable?,
) : Throwable(message, cause) {
    /** No credentials were available for this provider */
    class CredentialsNotLoaded internal constructor(
        val details: CredentialsNotLoadedDetails,
    ) : CredentialsError("the credential provider was not enabled", details.source)

    /** Loading credentials from this provider exceeded the maximum allowed duration */
    class ProviderTimedOut internal constructor(
        val details: ProviderTimedOutDetails,
    ) : CredentialsError(
            "credentials provider timed out after ${details.timeoutDuration().inWholeSeconds} seconds",
            null,
        )

    /**
     * The provider was given an invalid configuration
     *
     * For example:
     * - syntax error in ~/.aws/config
     * - assume role profile that forms an infinite loop
     */
    class InvalidConfiguration internal constructor(
        val details: InvalidConfigurationDetails,
    ) : CredentialsError("the credentials provider was not properly configured", details.source)

    /**
     * The provider experienced an error during credential resolution
     *
     * This may include errors like a 503 from STS or a file system error when attempting to
     * read a configuration file.
     */
    class ProviderError internal constructor(
        val details: ProviderErrorDetails,
    ) : CredentialsError("an error occurred while loading credentials", details.source)

    /**
     * An unexpected error occurred during credential resolution
     *
     * If the error is something that can occur during expected usage of a provider,
     * [ProviderError] should be returned instead. [Unhandled] is reserved for exceptional
     * cases, for example:
     * - Returned data not UTF-8
     * - A provider returns data that is missing required fields
     */
    class Unhandled internal constructor(
        val details: UnhandledDetails,
    ) : CredentialsError("unexpected credentials error", details.source)

    companion object {
        /**
         * The credentials provider did not provide credentials
         *
         * This error indicates the credentials provider was not enable or no configuration was set.
         * This contrasts with [invalidConfiguration], indicating that the provider was configured
         * in some way, but certain settings were invalid.
         */
        fun notLoaded(source: Throwable): CredentialsError =
            CredentialsNotLoaded(CredentialsNotLoadedDetails(source))

        /**
         * The credentials provider did not provide credentials
         *
         * This error indicates the credentials provider was not enable or no configuration was set.
         * This contrasts with [invalidConfiguration], indicating that the provider was configured
         * in some way, but certain settings were invalid.
         */
        fun notLoadedNoSource(): CredentialsError =
            CredentialsNotLoaded(CredentialsNotLoadedDetails(null))

        /**
         * An unexpected error occurred loading credentials from this provider
         *
         * Unhandled errors should not occur during normal operation and should be reserved for
         * exceptional cases, such as a JSON API returning an output that was not parseable as JSON.
         */
        fun unhandled(source: Throwable): CredentialsError =
            Unhandled(UnhandledDetails(source))

        /**
         * The credentials provider returned an error
         *
         * Provider errors may occur during normal use of a credentials provider, e.g. a 503 when
         * retrieving credentials from IMDS.
         */
        fun providerError(source: Throwable): CredentialsError =
            ProviderError(ProviderErrorDetails(source))

        /** The provided configuration for a provider was invalid */
        fun invalidConfiguration(source: Throwable): CredentialsError =
            InvalidConfiguration(InvalidConfigurationDetails(source))

        /** The credentials provider did not provide credentials within an allotted duration */
        fun providerTimedOut(timeoutDuration: Duration): CredentialsError =
            ProviderTimedOut(ProviderTimedOutDetails(timeoutDuration))
    }
}

/** Details for [TokenError.TokenNotLoaded] */
class TokenNotLoadedDetails internal constructor(
    internal val source: Throwable,
) {
    override fun toString(): String = "TokenNotLoaded(source=$source)"
}

/** Error returned when an access token provider fails to provide an access token. */
sealed class TokenError(
    message: String,
    cause: Throwable?,
) : Throwable(message, cause) {
    /** This provider couldn't provide a token. */
    class TokenNotLoaded internal constructor(
        val details: TokenNotLoadedDetails,
    ) : TokenError("the access token provider was not enabled", details.source)

    /** Loading a token from this provider exceeded the maximum allowed time. */
    class ProviderTimedOut internal constructor(
        val details: ProviderTimedOutDetails,
    ) : TokenError(
            "access token provider timed out after ${details.timeoutDuration().inWholeSeconds} seconds",
            null,
        )

    /**
     * The provider was given invalid configuration.
     *
     * For example, a syntax error in `~/.aws/config`.
     */
    class InvalidConfiguration internal constructor(
        val details: InvalidConfigurationDetails,
    ) : TokenError("the access token provider was not properly configured", details.source)

    /** The provider experienced an error during credential resolution. */
    class ProviderError internal constructor(
        val details: ProviderErrorDetails,
    ) : TokenError("an error occurred while loading an access token", details.source)

    /**
     * An unexpected error occurred during token resolution.
     *
     * If the error is something that can occur during expected usage of a provider,
     * [ProviderError] should be returned instead. [Unhandled] is reserved for exceptional
     * cases, for example:
     * - Returned data not UTF-8
     * - A provider returns data that is missing required fields
     */
    class Unhandled internal constructor(
        val details: UnhandledDetails,
    ) : TokenError("unexpected access token providererror", details.source)

    companion object {
        /**
         * The access token provider couldn't provide a token.
         *
         * This error indicates the token provider was not enable or no configuration was set.
         * This contrasts with [invalidConfiguration], indicating that the provider was configured
         * in some way, but certain settings were invalid.
         */
        fun notLoaded(source: Throwable): TokenError =
            TokenNotLoaded(TokenNotLoadedDetails(source))

        /**
         * An unexpected error occurred loading an access token from this provider.
         *
         * Unhandled errors should not occur during normal operation and should be reserved for
         * exceptional cases, such as a JSON API returning an output that was not parseable as JSON.
         */
        fun unhandled(source: Throwable): TokenError =
            Unhandled(UnhandledDetails(source))

        /** The access token provider returned an error. */
        fun providerError(source: Throwable): TokenError =
            ProviderError(ProviderErrorDetails(source))

        /** The provided configuration for a provider was invalid. */
        fun invalidConfiguration(source: Throwable): TokenError =
            InvalidConfiguration(InvalidConfigurationDetails(source))

        /** The access token provider did not provide a token within an allotted amount of time. */
        fun providerTimedOut(timeoutDuration: Duration): TokenError =
            ProviderTimedOut(ProviderTimedOutDetails(timeoutDuration))
    }
}
