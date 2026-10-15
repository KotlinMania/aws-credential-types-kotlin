// port-lint: source credentials_impl.rs
package io.github.kotlinmania.awscredentialtypes

import io.github.kotlinmania.awscredentialtypes.attributes.AccountId
import io.github.kotlinmania.awscredentialtypes.credentialfeature.AwsCredentialFeature
import io.github.kotlinmania.awscredentialtypes.provider.ProvideCredentials
import io.github.kotlinmania.zeroize.Zeroize
import io.github.kotlinmania.zeroize.ZeroizeOnDrop
import kotlin.reflect.KClass
import kotlin.time.Instant
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideCredentials as ProvideCredentialsFuture

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * AWS SDK Credentials
 *
 * An opaque struct representing credentials that may be used in an AWS SDK, modeled on
 * the [CRT credentials implementation](https://github.com/awslabs/aws-c-auth/blob/main/source/credentials.c).
 *
 * When `Credentials` is dropped, its contents are zeroed in memory. Credentials uses an interior
 * shared reference to ensure that even when cloned, credentials don't exist in multiple memory
 * locations.
 */
class Credentials private constructor(
    private val inner: Inner,
    @PublishedApi internal val properties: MutableMap<KClass<*>, Any>,
) : ProvideCredentials,
    Zeroize,
    ZeroizeOnDrop,
    AutoCloseable {
    override fun provideCredentials(): ProvideCredentialsFuture =
        ProvideCredentialsFuture.ready(Result.success(this.clone()))

    override fun zeroize() {
        inner.zeroize()
    }

    override fun close() {
        zeroize()
    }

    private class Inner(
        var accessKeyId: String,
        var secretAccessKey: String,
        var sessionToken: String?,
        // Credential Expiry
        //
        // An [Instant] at which the credentials should no longer be used because they have expired.
        // The primary purpose of this value is to allow credentials to communicate to the caching
        // provider when they need to be refreshed.
        //
        // If these credentials never expire, this value will be set to `null`.
        var expiresAfter: Instant?,
        // Optional piece of data to support account-based endpoints.
        // https://docs.aws.amazon.com/sdkref/latest/guide/feature-account-endpoints.html
        val accountId: AccountId?,
        val providerName: String,
    ) : Zeroize,
        ZeroizeOnDrop {
        override fun zeroize() {
            accessKeyId = ""
            secretAccessKey = ""
            sessionToken = null
        }

        override fun equals(other: Any?): Boolean =
            this === other ||
                (
                    other is Inner &&
                        accessKeyId == other.accessKeyId &&
                        secretAccessKey == other.secretAccessKey &&
                        sessionToken == other.sessionToken &&
                        expiresAfter == other.expiresAfter &&
                        accountId == other.accountId &&
                        providerName == other.providerName
                )

        override fun hashCode(): Int {
            var result = accessKeyId.hashCode()
            result = 31 * result + secretAccessKey.hashCode()
            result = 31 * result + (sessionToken?.hashCode() ?: 0)
            result = 31 * result + (expiresAfter?.hashCode() ?: 0)
            result = 31 * result + (accountId?.hashCode() ?: 0)
            result = 31 * result + providerName.hashCode()
            return result
        }
    }

    fun clone(): Credentials = Credentials(inner, properties.toMutableMap())

    override fun equals(other: Any?): Boolean =
        this === other || (other is Credentials && inner == other.inner)

    override fun hashCode(): Int = inner.hashCode()

    override fun toString(): String {
        val parts = mutableListOf<String>()
        parts += "provider_name: \"${inner.providerName}\""
        parts += "access_key_id: \"${inner.accessKeyId}\""
        parts += "secret_access_key: \"** redacted **\""
        val expiry = expiry()
        if (expiry != null) {
            parts += "expires_after: \"$expiry\""
        } else {
            parts += "expires_after: \"never\""
        }
        val accountId = inner.accountId
        if (accountId != null) {
            parts += "account_id: \"${accountId.asStr()}\""
        }
        properties.values.forEachIndexed { i, prop ->
            parts += "property_$i: $prop"
        }
        return "Credentials { ${parts.joinToString(", ")} }"
    }

    /** Returns the access key ID. */
    fun accessKeyId(): String = inner.accessKeyId

    /** Returns the secret access key. */
    fun secretAccessKey(): String = inner.secretAccessKey

    /** Returns the time when the credentials will expire. */
    fun expiry(): Instant? = inner.expiresAfter

    /** Returns the time when the credentials will expire. */
    fun expiryMut(): Instant? = inner.expiresAfter

    /** Sets the time when the credentials will expire. */
    fun setExpiry(expiry: Instant?) {
        inner.expiresAfter = expiry
    }

    /** Returns the account ID. */
    fun accountId(): AccountId? = inner.accountId

    /** Returns the session token. */
    fun sessionToken(): String? = inner.sessionToken

    /** Set arbitrary property for [Credentials] */
    inline fun <reified T : Any> setProperty(prop: T) {
        properties[T::class] = prop
    }

    /** Returns arbitrary property associated with these [Credentials]. */
    inline fun <reified T : Any> getProperty(): T? = properties[T::class] as? T

    /** Attempts to retrieve a property of a given type [T]. */
    inline fun <reified T : Any> getPropertyMut(): T? = getProperty<T>()

    /**
     * Returns a property of a given type [T] if it is stored on these [Credentials],
     * otherwise stores and returns the value produced by [default].
     */
    inline fun <reified T : Any> getPropertyOrDefault(default: () -> T): T {
        val key: KClass<*> = T::class
        val existing = properties[key]
        if (existing != null) {
            return existing as T
        }
        val created = default()
        properties[key] = created
        return created
    }

    /**
     * Returns a property of a given type [T] if it is stored on these [Credentials],
     * otherwise stores and returns the value produced by [default].
     */
    inline fun <reified T : Any> getPropertyMutOrDefault(default: () -> T): T = getPropertyOrDefault(default)

    /** Converts these credentials to an [Identity]. */
    fun toIdentity(): Identity = Identity.from(this)

    companion object {
        private const val STATIC_CREDENTIALS: String = "Static"

        /** Returns builder for [Credentials]. */
        fun builder(): CredentialsBuilder = CredentialsBuilder()

        /**
         * Creates [Credentials].
         *
         * This is intended to be used from a custom credentials provider implementation.
         * It is __NOT__ secure to hardcode credentials into your application.
         */
        fun create(
            accessKeyId: String,
            secretAccessKey: String,
            sessionToken: String?,
            expiresAfter: Instant?,
            providerName: String,
        ): Credentials =
            Credentials(
                Inner(
                    accessKeyId = accessKeyId,
                    secretAccessKey = secretAccessKey,
                    sessionToken = sessionToken,
                    expiresAfter = expiresAfter,
                    accountId = null,
                    providerName = providerName,
                ),
                mutableMapOf(),
            )

        /**
         * Creates [Credentials].
         *
         * Alias for [create] matching upstream Rust `Credentials::new`.
         */
        fun new(
            accessKeyId: String,
            secretAccessKey: String,
            sessionToken: String?,
            expiresAfter: Instant?,
            providerName: String,
        ): Credentials =
            create(
                accessKeyId = accessKeyId,
                secretAccessKey = secretAccessKey,
                sessionToken = sessionToken,
                expiresAfter = expiresAfter,
                providerName = providerName,
            )

        /**
         * Creates [Credentials] from hardcoded access key, secret key, and session token.
         *
         * _Note: In general, you should prefer to use the credential providers that come
         * with the AWS SDK to get credentials. It is __NOT__ secure to hardcode credentials
         * into your application. If you're writing a custom credentials provider, then
         * use [Credentials.create] instead of this._
         */
        fun fromKeys(
            accessKeyId: String,
            secretAccessKey: String,
            sessionToken: String?,
        ): Credentials =
            create(
                accessKeyId,
                secretAccessKey,
                sessionToken,
                null,
                STATIC_CREDENTIALS,
            )

        /** Creates a test [Credentials] with no session token. */
        fun forTests(): Credentials =
            create(
                "ANOTREAL",
                "notrealrnrELgWzOk3IfjzDKtFBhDby",
                null,
                null,
                "test",
            )

        /** Creates a test [Credentials] that include a session token. */
        fun forTestsWithSessionToken(): Credentials =
            create(
                "ANOTREAL",
                "notrealrnrELgWzOk3IfjzDKtFBhDby",
                "notarealsessiontoken",
                null,
                "test",
            )

        internal fun build(builder: CredentialsBuilder): Credentials {
            val accessKeyId =
                builder.accessKeyId
                    ?: throw IllegalStateException("required field `access_key_id` missing")
            val secretAccessKey =
                builder.secretAccessKey
                    ?: throw IllegalStateException("required field `secret_access_key` missing")
            val providerName =
                builder.providerName
                    ?: throw IllegalStateException("required field `provider_name` missing")
            return Credentials(
                Inner(
                    accessKeyId = accessKeyId,
                    secretAccessKey = secretAccessKey,
                    sessionToken = builder.sessionToken,
                    expiresAfter = builder.expiresAfter,
                    accountId = builder.accountId,
                    providerName = providerName,
                ),
                mutableMapOf(),
            )
        }
    }
}

/**
 * Builder for [Credentials]
 *
 * Similar to [Credentials.create], the use of the builder is intended for a custom credentials
 * provider implementation. It is __NOT__ secure to hardcode credentials into your application.
 */
class CredentialsBuilder {
    internal var accessKeyId: String? = null
    internal var secretAccessKey: String? = null
    internal var sessionToken: String? = null
    internal var expiresAfter: Instant? = null
    internal var accountId: AccountId? = null
    internal var providerName: String? = null

    /** Set access key id for the builder. */
    fun accessKeyId(accessKeyId: String): CredentialsBuilder {
        this.accessKeyId = accessKeyId
        return this
    }

    /** Set secret access key for the builder. */
    fun secretAccessKey(secretAccessKey: String): CredentialsBuilder {
        this.secretAccessKey = secretAccessKey
        return this
    }

    /** Set session token for the builder. */
    fun sessionToken(sessionToken: String): CredentialsBuilder {
        setSessionToken(sessionToken)
        return this
    }

    /** Set session token for the builder. */
    fun setSessionToken(sessionToken: String?) {
        this.sessionToken = sessionToken
    }

    /** Set expiry for the builder. */
    fun expiry(expiry: Instant): CredentialsBuilder {
        setExpiry(expiry)
        return this
    }

    /** Set expiry for the builder. */
    fun setExpiry(expiry: Instant?) {
        this.expiresAfter = expiry
    }

    /** Set account ID for the builder. */
    fun accountId(accountId: AccountId): CredentialsBuilder {
        setAccountId(accountId)
        return this
    }

    /** Set account ID for the builder. */
    fun accountId(accountId: String): CredentialsBuilder {
        setAccountId(AccountId.from(accountId))
        return this
    }

    /** Set account ID for the builder. */
    fun setAccountId(accountId: AccountId?) {
        this.accountId = accountId
    }

    /** Set provider name for the builder. */
    fun providerName(providerName: String): CredentialsBuilder {
        this.providerName = providerName
        return this
    }

    /** Build [Credentials] from the builder. */
    fun build(): Credentials = Credentials.build(this)

    companion object {
        /**
         * Creates a test [CredentialsBuilder] with the required fields:
         * `accessKeyId`, `secretAccessKey`, and `providerName`.
         */
        fun forTests(): CredentialsBuilder =
            CredentialsBuilder()
                .accessKeyId("ANOTREAL")
                .secretAccessKey("notrealrnrELgWzOk3IfjzDKtFBhDby")
                .providerName("test")
    }
}

/**
 * Identity representing an authenticated client identity in the AWS SDK,
 * mirroring upstream `aws_smithy_runtime_api::client::identity::Identity`.
 */
class Identity(
    val data: Any,
    val expiration: Instant? = null,
    private val properties: Map<KClass<*>, Any> = emptyMap(),
) {
    /** Retrieves property of type [T] associated with this identity. */
    inline fun <reified T : Any> property(): T? = property(T::class)

    /** Retrieves property of type [klass] associated with this identity. */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> property(klass: KClass<T>): T? = properties[klass] as? T

    companion object {
        /**
         * Creates an [Identity] from [Credentials], mirroring upstream identity conversion.
         */
        fun from(credentials: Credentials): Identity {
            val props = mutableMapOf<KClass<*>, Any>()
            val accountId = credentials.accountId()
            if (accountId != null) {
                props[AccountId::class] = accountId
            }
            val existingFeatures = credentials.getProperty<List<AwsCredentialFeature>>().orEmpty()
            val features = existingFeatures.toMutableList()
            if (accountId != null && !features.contains(AwsCredentialFeature.RESOLVED_ACCOUNT_ID)) {
                features.add(AwsCredentialFeature.RESOLVED_ACCOUNT_ID)
            }
            if (features.isNotEmpty()) {
                props[List::class] = features
            }
            return Identity(
                data = credentials,
                expiration = credentials.expiry(),
                properties = props,
            )
        }
    }
}

/**
 * Creates an [Identity] from [Credentials], mirroring upstream identity conversion.
 */
fun from(credentials: Credentials): Identity = Identity.from(credentials)
