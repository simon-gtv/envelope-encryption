package com.gtv.security.stub;

import static com.gtv.security.EncryptionEnvelope.toByteArray;

import java.util.AbstractMap.SimpleEntry;

import com.gtv.security.EncryptionEnvelope;
import com.gtv.security.KeySource;

public class KeySourceStub implements KeySource {

   public static final String ENCRYPTED_PREF = "ENCRYPTED-KEY";
   public static int          i              = 0;
   public static String       KEY_PREF       = "PLAIN-TEXT-KEY-";

   @Override
   public SimpleEntry<byte[], byte[]> createKey() {

      i++;
      return new SimpleEntry<>(toByteArray(KEY_PREF + i), toByteArray(ENCRYPTED_PREF + i));
   }

   @Override
   public byte[] decryptKey(byte[] encryptedKey) {

      String string = EncryptionEnvelope.toString(encryptedKey);
      string = KEY_PREF + string.substring(string.length() - 1);
      return toByteArray(string);
   }

}
