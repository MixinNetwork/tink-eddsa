package one.mixin.eddsa.example;

import okio.ByteString;
import one.mixin.eddsa.Ed25519Sign;
import one.mixin.eddsa.Ed25519Verify;
import one.mixin.eddsa.KeyPair;

public class Example {
  public static void main(String[] args) {
    KeyPair keyPair = KeyPair.newKeyPair();
    Ed25519Sign signer = Ed25519Sign.invoke(keyPair.getPrivateKey());
    Ed25519Verify verifier = new Ed25519Verify(keyPair.getPublicKey());

    ByteString msg = ByteString.of("Hello world".getBytes());
    ByteString sig = signer.sign(msg);
    boolean valid = verifier.verify(sig, msg);
    System.out.println("valid: " + valid);
  }
}
