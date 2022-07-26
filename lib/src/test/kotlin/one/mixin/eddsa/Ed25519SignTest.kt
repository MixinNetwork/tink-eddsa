// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// //////////////////////////////////////////////////////////////////////////////
package one.mixin.eddsa

import okio.ByteString.Companion.toByteString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.fail
import org.junit.Test
import java.util.TreeSet

/**
 * Tink's unit tests for [Ed25519Sign].
 */
class Ed25519SignTest {
    @Test
    fun testSigningOneKeyWithMultipleMessages() {
        val keyPair = KeyPair.newKeyPair()
        val signer = Ed25519Sign(keyPair.privateKey)
        val verifier = Ed25519Verify(keyPair.publicKey)
        for (i in 0..99) {
            val msg = Random.randBytes(20)
            val sig = signer.sign(msg)
            if (!verifier.verify(sig, msg)) {
                fail(
                    """
          |Message: ${msg.hex()}
          |Signature: ${sig.hex()}
          |PrivateKey: ${keyPair.privateKey.hex()}
          |PublicKey: ${keyPair.publicKey.hex()}
                    """.trimMargin(),
                )
            }
        }
    }

    @Test
    fun testSigningOneKeyWithTheSameMessage() {
        val keyPair = KeyPair.newKeyPair()
        val signer = Ed25519Sign(keyPair.privateKey)
        val verifier = Ed25519Verify(keyPair.publicKey)
        val msg = Random.randBytes(20)
        val allSignatures = TreeSet<String>()
        for (i in 0..99) {
            val sig = signer.sign(msg)
            allSignatures.add(sig.hex())
            if (!verifier.verify(sig, msg)) {
                fail(
                    """
          |Message: ${msg.hex()}
          |Signature: ${sig.hex()}
          |PrivateKey: ${keyPair.privateKey.hex()}
          |PublicKey: ${keyPair.publicKey.hex()}
                    """.trimMargin(),
                )
            }
        }
        // Ed25519 is deterministic, expect a unique signature for the same message.
        assertEquals(1, allSignatures.size.toLong())
    }

    @Test
    fun testSignWithPrivateKeyLengthDifferentFrom32Byte() {
        assertThrows(IllegalArgumentException::class.java) {
            Ed25519Sign(ByteArray(31).toByteString())
        }
        assertThrows(IllegalArgumentException::class.java) {
            Ed25519Sign(ByteArray(33).toByteString())
        }
    }

    @Test
    fun testSigningWithMultipleRandomKeysAndMessages() {
        for (i in 0..99) {
            val keyPair = KeyPair.newKeyPair()
            val signer = Ed25519Sign(keyPair.privateKey)
            val verifier = Ed25519Verify(keyPair.publicKey)
            val msg = Random.randBytes(20)
            val sig = signer.sign(msg)
            if (!verifier.verify(sig, msg)) {
                fail(
                    """
          |Message: ${msg.hex()}
          |Signature: ${sig.hex()}
          |PrivateKey: ${keyPair.privateKey.hex()}
          |PublicKey: ${keyPair.publicKey.hex()}
                    """.trimMargin(),
                )
            }
        }
    }

    @Test
    fun testKeyPairFromSeedTooShort() {
        val keyMaterial = Random.randBytes(10)
        assertThrows(IllegalArgumentException::class.java) {
            KeyPair.newKeyPairFromSeed(keyMaterial)
        }
    }
}
