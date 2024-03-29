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

import okio.ByteString
import one.mixin.eddsa.Ed25519.getHashedScalar
import one.mixin.eddsa.Ed25519.scalarMultWithBaseToBytes
import one.mixin.eddsa.Ed25519.sign

/**
 * Ed25519 signing.
 *
 * # Usage
 *
 * ```
 * val keyPair = Ed25519Sign.KeyPair.newKeyPair()
 *
 * // securely store keyPair and share keyPair.getPublicKey()
 * val signer = Ed25519Sign(keyPair.getPrivateKey())
 * val signature = signer.sign(message)
 * ```
 */
class Ed25519Sign private constructor(
    private val hashedPrivateKey: ByteString,
    private val publicKey: ByteString,
) {
    fun sign(data: ByteString, checkOnCurve: Boolean): ByteString {
        return sign(data, publicKey, hashedPrivateKey, checkOnCurve)
    }

    companion object {
        private const val SECRET_KEY_LEN = Field25519.FIELD_LEN

        /**
         * @param privateKey 32-byte random sequence.
         */
        @JvmStatic
        operator fun invoke(privateKey: ByteString, checkOnCurve: Boolean): Ed25519Sign {
            require(privateKey.size == SECRET_KEY_LEN) {
                "Given private key's length is not $SECRET_KEY_LEN"
            }
            val hashedPrivateKey = getHashedScalar(privateKey)
            val publicKey = scalarMultWithBaseToBytes(hashedPrivateKey, checkOnCurve)
            return Ed25519Sign(hashedPrivateKey, publicKey)
        }
    }
}
