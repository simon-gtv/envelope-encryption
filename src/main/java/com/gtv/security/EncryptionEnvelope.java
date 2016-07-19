package com.gtv.security;

import java.io.UnsupportedEncodingException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionEnvelope {

   private static final String AES = "AES";

   private static final Cipher CIPHER;
   private static final String ISO_8859_1 = "ISO-8859-1"; // 8-bit (byte)
                                                          // transparent
                                                          // char-set
   @Autowired
   private JpaDelegate         dao;

   @Autowired
   private KeySource keySource;

   static {
      try {
         CIPHER = Cipher.getInstance(AES);
      }
      catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }
   }

   public static byte[] toByteArray(String string) {

      try {
         return string.getBytes(ISO_8859_1);
      }
      catch (UnsupportedEncodingException e) {
         throw new EncryptionEnvelopeException(e);
      }
   }

   public static String toString(byte[] bytes) {

      try {
         return new String(bytes, ISO_8859_1);
      }
      catch (UnsupportedEncodingException e) {
         throw new EncryptionEnvelopeException(e);
      }

   }

   private byte[] decrypt(byte[] key, byte[] value) {

      byte[] original;
      try {
         SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
         value = Base64.getDecoder().decode(value);
         CIPHER.init(Cipher.DECRYPT_MODE, secretKeySpec);
         original = CIPHER.doFinal(value);
      }
      catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }
      return original;
   }

   private byte[] encrypt(byte[] plainKey, byte[] target) {

      try {
         SecretKeySpec secretKeySpec = new SecretKeySpec(plainKey, AES);
         CIPHER.init(Cipher.ENCRYPT_MODE, secretKeySpec);
         target = CIPHER.doFinal(target);
         target = Base64.getEncoder().encode(target);
         return target;
      }
      catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }

   }

   public byte[] fetch(String id) {

      SimpleEntry<String, byte[]> data = dao.fetchData(id);
      if (data == null)
         return null;
      byte[] encryptedData = data.getValue();
      byte[] encryptedKey = dao.fetchKey(data.getKey());
      byte[] key = keySource.decryptKey(encryptedKey);
      return decrypt(key, encryptedData);
   }

   public void setJpaDelegate(JpaDelegate jpa) {

      this.dao = jpa;
   }

   public void setKeySource(KeySource keySource) {

      this.keySource = keySource;
   }

   public void store(String id, byte[] data, String keyId) {

      byte[] encryptedKey = dao.fetchKey(keyId);
      byte[] key = null;
      if (encryptedKey == null) {
         SimpleEntry<byte[], byte[]> plainAndEncrypted = keySource.createKey();
         dao.saveKey(keyId, plainAndEncrypted.getValue());
         key = plainAndEncrypted.getKey();
      } else {
         key = keySource.decryptKey(encryptedKey);
      }
      byte[] encryptedData = encrypt(key, data);
      dao.saveData(id, encryptedData, keyId);
   }
}
