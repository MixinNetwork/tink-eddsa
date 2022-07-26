# tink-eddsa

## Installation

### gradle
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```
dependencies {
    implementation 'com.github.MixinNetwork:tink-eddsa:0.0.2'
}
```

## Usage

### Kotlin

```kotlin
fun main() {
  val keyPair = KeyPair.newKeyPair()
  val signer = Ed25519Sign(keyPair.privateKey)
  val verifier = Ed25519Verify(keyPair.publicKey)

  val msg = "Hello world".toByteArray().toByteString()
  val sig = signer.sign(msg)
  val valid = verifier.verify(sig, msg)
  println("valid: $valid")
}
```

### Java

```java
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
```