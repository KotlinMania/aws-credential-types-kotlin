package io.github.kotlinmania.awscredentialtypes

import io.github.kotlinmania.awscredentialtypes.provider.token.ProvideToken
import io.github.kotlinmania.awscredentialtypes.provider.token.TokenResult
import kotlin.time.Instant
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideToken as ProvideTokenFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
/**
 * AWS Access Token
 *
 * This access token type is used to authenticate to AWS services that use HTTP Bearer
 * Auth with an AWS Builder ID such as CodeCatalyst.
 *
 * For more details on tokens, see: <https://oauth.net/2/access-tokens>
 */
class Token private constructor(
    private val token: String,
    private val expiresAfter: Instant?,
) : ProvideToken {
    /** Returns the bearer token string. */
    fun token(): String = token

    /** Returns the instant when the token expires, when one is available. */
    fun expiration(): Instant? = expiresAfter

    /** Returns the instant when the token expires, matching [expiration]. */
    fun expiresAfter(): Instant? = expiresAfter

    /** Returns an equivalent token value. */
    fun clone(): Token = Token(token, expiresAfter)

    /** Converts this token to an [Identity]. */
    fun toIdentity(): Identity =
        Identity(
            data = this,
            expiration = expiresAfter,
        )

    override fun provideToken(): ProvideTokenFuture =
        ProvideTokenFuture.ready(TokenResult.success(clone()))

    override fun equals(other: Any?): Boolean =
        this === other ||
            (
                other is Token &&
                    token == other.token &&
                    expiresAfter == other.expiresAfter
            )

    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + expiresAfter.hashCode()
        return result
    }

    override fun toString(): String {
        val expiry = expiresAfter?.let { ", expires_after: \"$it\"" }.orEmpty()
        return "Token { token: \"** redacted **\"$expiry }"
    }

    companion object {
        /** Creates an AWS access token from the token string and optional expiration. */
        fun new(
            token: String,
            expiresAfter: Instant?,
        ): Token = Token(token, expiresAfter)
    }
}
