# tink-eddsa
The original Java code of this repo is from [Google Tink](https://github.com/google/tink), and has been translated to Kotlin by [Cashapp zipline](https://github.com/cashapp/zipline).

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
    implementation 'com.github.mixinnetwork:tink-eddsa:0.0.6'
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

## Benchmark

MacBook Pro (16-inch, 2019), macOS Monterey, with 2.4GHz i9 32GB

| type |    rate    | iterations    |   time  | data throughput |
|----------|:----------:|---------------|:-------:|:---------------:|
| sign | 140.60 MB/s | 703 iterations | 5002 ms | 703.00 MB |
| verify | 285.20 MB/s | 1426 iterations | 5000 ms | 1.39 GB |