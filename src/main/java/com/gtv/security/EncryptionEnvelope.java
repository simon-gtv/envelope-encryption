package com.gtv.security;

import java.util.AbstractMap.SimpleEntry;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;

public class EncryptionEnvelope {

   private static final String ENCRYPTED = "Encrypted";
   private static final String DECRYPTED = "Decrypted";
   private static final String AES       = "AES";
   private static final String UTF8      = "UTF-8";

   @Autowired
   private JpaDelegate saver;
   @Autowired
   private KeySource   keySource;

   private EncryptionEnvelopeResult<String> encrypt(String plainKey, String id, String target) {

      try {
         byte[] key = plainKey.getBytes(UTF8);
         byte[] value = target.getBytes(UTF8);
         SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
         Cipher cipher = Cipher.getInstance(AES);
         cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
         byte[] encrypted = cipher.doFinal(value);
         return new EncryptionEnvelopeResult<String>(true, id + " " + ENCRYPTED, new String(encrypted, UTF8));
      } catch (Exception e) {
         return new EncryptionEnvelopeResult<String>(false, e.getMessage(), null);
      }

   }

   private EncryptionEnvelopeResult<String> decrypt(String plainKey, String id, String target) {

      try {
         byte[] key = plainKey.getBytes(UTF8);
         byte[] value = target.getBytes(UTF8);
         Cipher cipher = Cipher.getInstance(AES);
         SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
         cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
         byte[] original = cipher.doFinal(value);
         return new EncryptionEnvelopeResult<String>(false, id + " " + DECRYPTED, new String(original, UTF8));
      } catch (Exception e) {
         return new EncryptionEnvelopeResult<String>(false, e.getMessage(), null);
      }

   }

   @SuppressWarnings("unchecked")
   public EncryptionEnvelopeResult<?> fetch(String id) {

      EncryptionEnvelopeResult<?> result = saver.fetchData(id);
      if (!result.isSuccess())
         return result;

      SimpleEntry<String, String> data = (SimpleEntry<String, String>) result.getPayload();
      String keyId = data.getKey();
      String encryptedData = data.getValue();

      result = saver.fetchKey(keyId);
      if (!result.isSuccess())
         return result;

      result = saver.fetchKey(keyId);
      if (!result.isSuccess())
         return result;

      return decrypt(result.getPayload().toString(), id, encryptedData);

   }

   public EncryptionEnvelopeResult<?> store(String id, String data, String keyId) {

      String key = null;
      
      EncryptionEnvelopeResult<?> result = saver.fetchKey(keyId);
      if (result.isSuccess() ) {
         result = keySource.decryptKey(result.getPayload().toString());
         if (!result.isSuccess())
            return result;

         key = result.getPayload().toString();     
         
         
         result = createKey(keyId);
      } else if() {
         return result;
      }

      result = keySource.decryptKey(result.getPayload().toString());
      if (!result.isSuccess())
         return result;

      key = result.getPayload().toString();

      result = encrypt(key, id, data);
      if (!result.isSuccess())
         return result;

      return saver.saveData(id, result.getPayload().toString(), keyId);
   }

   public void setJpaDelegate(JpaDelegate jpa) {

      this.saver = jpa;
   }

   public void setKeySource(KeySource keySource) {

      this.keySource = keySource;
   }

}
