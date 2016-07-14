package com.gtv.security;

public interface KeySource {

   EncryptionEnvelopeResult<Void> createKey(String keyName);

   EncryptionEnvelopeResult<String> decryptKey(String keyName);

}
