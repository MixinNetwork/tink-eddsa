package one.mixin.eddsa

import net.i2p.crypto.eddsa.EdDSAEngine
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec
import okio.ByteString.Companion.toByteString
import org.junit.Test
import java.security.MessageDigest
import java.security.PrivateKey
import kotlin.random.Random

class SignVerifyBenchmark {
  @Test
  fun testBenchmark() {
    runBenchmark(true)
    runBenchmark(false)
  }

  private fun runBenchmark(forSigning: Boolean) {
    val keyPair = KeyPair.newKeyPair()
    val signer = Ed25519Sign(keyPair.privateKey)
    val verifier = Ed25519Verify(keyPair.publicKey)

    var totalData = 0.0
    var iterations = 0
    val timeout = 5000L
    val signData = Random.nextBytes(1024 * 1024).toByteString()
    val sig = signer.sign(signData)

    var timeElapsed = 0L
    val time = System.currentTimeMillis()
    while (timeElapsed < timeout) {
      if (forSigning) {
        signer.sign(signData)
      } else {
        verifier.verify(sig, signData)
      }

      iterations++
      totalData+=signData.size
      val newTime = System.currentTimeMillis()
      timeElapsed = newTime - time
    }

    val s = timeElapsed / 1000
    println("| ${if (forSigning) "sign" else "verify"} | ${formatDataLength(totalData / s)}/s | $iterations iterations | $timeElapsed ms | ${formatDataLength(totalData)} |")
  }

  @Test
  fun testBenchmarkEd() {
    runBenchmarkEd(true)
    runBenchmarkEd(false)
  }

  private fun runBenchmarkEd(forSigning: Boolean) {
    val spec: EdDSAParameterSpec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519)
    val engine = EdDSAEngine(MessageDigest.getInstance(spec.hashAlgorithm))
    val seed = Random.nextBytes(32)
    val privKey = EdDSAPrivateKeySpec(seed, spec)
    val sKey: PrivateKey = EdDSAPrivateKey(privKey)
    val pKey = EdDSAPublicKey(EdDSAPublicKeySpec(privKey.a, spec))

    var totalData = 0.0
    var iterations = 0
    val timeout = 5000L
    val signData = Random.nextBytes(1024 * 1024)

    if (forSigning) {

      var timeElapsed = 0L
      val time = System.currentTimeMillis()
      while (timeElapsed < timeout) {
        engine.initSign(sKey)
        engine.update(signData)
        engine.sign()

        iterations++
        totalData+=signData.size
        val newTime = System.currentTimeMillis()
        timeElapsed = newTime - time
      }

      val s = timeElapsed / 1000
      println("| ${if (forSigning) "sign" else "verify"} | ${formatDataLength(totalData / s)}/s | $iterations iterations | $timeElapsed ms | ${formatDataLength(totalData)} |")
    } else {
      engine.initSign(sKey)
      engine.update(signData)
      val sig = engine.sign()

      var timeElapsed = 0L
      val time = System.currentTimeMillis()
      while (timeElapsed < timeout) {
        engine.initVerify(pKey)
        engine.update(signData)
        engine.verify(sig)

        iterations++
        totalData+=signData.size
        val newTime = System.currentTimeMillis()
        timeElapsed = newTime - time
      }

      val s = timeElapsed / 1000
      println("| ${if (forSigning) "sign" else "verify"} | ${formatDataLength(totalData / s)}/s | $iterations iterations | $timeElapsed ms | ${formatDataLength(totalData)} |")
    }
  }

  private fun formatDataLength(dataLen: Double): String {
    return if (dataLen < 1024) {
      "${dataLen.f2()} B"
    } else if (dataLen < 1024 * 1024) {
      "${(dataLen / 1024).f2()} KB"
    } else if (dataLen < 1024 * 1024 * 1024) {
      "${(dataLen / (1024 * 1024)).f2()} MB"
    } else {
      "${(dataLen / (1024 * 1024 * 1024)).f2()} GB"
    }
  }

  private fun Double.f2(): String = "%.2f".format(this)
}