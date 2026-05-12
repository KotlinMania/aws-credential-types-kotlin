// port-lint: source src/attributes.rs
package io.github.kotlinmania.awscredentialtypes.attributes

import kotlin.test.Test
import kotlin.test.assertEquals

class AttributesTest {
    @Test
    fun accountIdCreation() {
        val expected = "012345678901"
        assertEquals(expected, AccountId.from(expected).asStr())
    }
}
