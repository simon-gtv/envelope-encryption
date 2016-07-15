package com.gtv.security;

import java.util.AbstractMap.SimpleEntry;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;

public class EncryptionEnvelope {

   private static final String AES  = "AES";
   private static final String UTF8 = "UTF-8";
   private static final Cipher cipher;
   @Autowired
   private JpaDelegate         saver;
   @Autowired
   private KeySource           keySource;

   static {
      try {
         cipher = Cipher.getInstance(AES);
      } catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }
   }

   private String encrypt(String plainKey, String target) {

      try {
         byte[] key = plainKey.getBytes(UTF8);
         byte[] value = target.getBytes(UTF8);
         SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
         cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
         value = cipher.doFinal(value);
         value = Base64.getEncoder().encode(value);
         return new String(value, UTF8);
      } catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }

   }

   private String decrypt(String plainKey, String target) {

      try {
         byte[] key = plainKey.getBytes(UTF8);
         byte[] value = target.getBytes(UTF8);
         SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
         value = Base64.getDecoder().decode(value);
         cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
         byte[] original = cipher.doFinal(value);
         return new String(original, UTF8);
      } catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }
   }

   public String fetch(String id) {

      SimpleEntry<String, String> data = saver.fetchData(id);
      if (data == null)
         return null;
      String keyId = data.getKey();
      String encryptedData = data.getValue();
      String encryptedKey = saver.fetchKey(keyId);
      String key = keySource.decryptKey(encryptedKey);
      return decrypt(key, encryptedData);
   }

   public void store(String id, String data, String keyId) {

      String encryptedKey = saver.fetchKey(keyId);
      String key = null;
      if (encryptedKey == null) {
         SimpleEntry<String, String> plainAndEncrypted = keySource.createKey();
         saver.saveKey(keyId, plainAndEncrypted.getValue());
         key = plainAndEncrypted.getKey();
      } else {
         key = keySource.decryptKey(encryptedKey);
      }
      String encryptedData = encrypt(key, data);
      saver.saveData(id, encryptedData, keyId);
   }

   public void setJpaDelegate(JpaDelegate jpa) {

      this.saver = jpa;
   }

   public void setKeySource(KeySource keySource) {

      this.keySource = keySource;
   }

}
