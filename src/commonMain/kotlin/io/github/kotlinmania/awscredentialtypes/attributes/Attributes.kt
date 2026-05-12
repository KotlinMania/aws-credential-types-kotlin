// port-lint: source src/attributes.rs
package io.github.kotlinmania.awscredentialtypes.attributes

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// Types representing specific pieces of data contained within credentials or within token

/** Type representing a unique identifier representing an AWS account. */
class AccountId private constructor(private val inner: String) {
    /** Return the string equivalent of this account id. */
    fun asStr(): String = inner

    override fun equals(other: Any?): Boolean =
        this === other || (other is AccountId && inner == other.inner)

    override fun hashCode(): Int = inner.hashCode()

    override fun toString(): String = "AccountId(inner=\"$inner\")"

    companion object {
        /** Build an [AccountId] from any value convertible to a [String]. */
        fun from(value: String): AccountId = AccountId(value)
    }
}
