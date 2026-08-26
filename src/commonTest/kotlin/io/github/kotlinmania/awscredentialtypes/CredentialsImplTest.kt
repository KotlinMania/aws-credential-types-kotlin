// port-lint: source credentials_impl.rs
package io.github.kotlinmania.awscredentialtypes

import io.github.kotlinmania.awscredentialtypes.credentialfeature.AwsCredentialFeature
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class CredentialsImplTest {
    @Test
    fun debugImpl() {
        val creds =
            Credentials.create(
                "akid",
                "secret",
                "token",
                Instant.fromEpochSeconds(1234567890),
                "debug tester",
            )
        assertEquals(
            "Credentials { provider_name: \"debug tester\", access_key_id: \"akid\", " +
                "secret_access_key: \"** redacted **\", expires_after: \"2009-02-13T23:31:30Z\" }",
            creds.toString(),
        )

        val credsWithAccountId =
            Credentials
                .builder()
                .accessKeyId("akid")
                .secretAccessKey("secret")
                .sessionToken("token")
                .expiry(Instant.fromEpochSeconds(1234567890))
                .accountId("012345678901")
                .providerName("debug tester")
                .build()
        assertEquals(
            "Credentials { provider_name: \"debug tester\", access_key_id: \"akid\", " +
                "secret_access_key: \"** redacted **\", expires_after: \"2009-02-13T23:31:30Z\", " +
                "account_id: \"012345678901\" }",
            credsWithAccountId.toString(),
        )
    }

    @Test
    fun equalityIgnoresProperties() {
        val creds1 = Credentials.forTestsWithSessionToken()
        creds1.setProperty(AwsCredentialFeature.CREDENTIALS_CODE)

        val creds2 = Credentials.forTestsWithSessionToken()
        creds2.setProperty(SomeOtherProp("foo"))

        assertEquals(creds1, creds2)
    }

    @Test
    fun expirySetters() {
        val creds = Credentials.forTests()
        assertEquals(null, creds.expiry())
        val newExpiry = Instant.fromEpochSeconds(0) + 60.seconds
        creds.setExpiry(newExpiry)
        assertEquals(newExpiry, creds.expiry())
    }

    @Test
    fun identityInheritsFeatureProperties() {
        val creds = Credentials.forTestsWithSessionToken()
        val featureProps =
            listOf(
                AwsCredentialFeature.CREDENTIALS_CODE,
                AwsCredentialFeature.CREDENTIALS_STS_SESSION_TOKEN,
            )
        creds.setProperty(featureProps)
        val retrieved = creds.getProperty<List<AwsCredentialFeature>>()
        assertEquals(featureProps, retrieved)
    }

    @Test
    fun fromCredentialsAddsResolvedAccountIdFeature() {
        val creds =
            Credentials.builder()
                .accessKeyId("test")
                .secretAccessKey("test")
                .accountId("123456789012")
                .providerName("test")
                .build()
        assertEquals("123456789012", creds.accountId()?.asStr())
    }

    private data class SomeOtherProp(
        val value: String,
    )
}
