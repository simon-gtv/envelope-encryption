package com.gtv.security.stub;

import java.util.AbstractMap.SimpleEntry;

import com.gtv.security.KeySource;

public class KeySourceStub implements KeySource {

   public static final String ENCRYPTED_PREF = "ENCRYPTED-KEY";
   public static String       KEY_PREF       = "PLAIN-TEXT-KEY-";
   public static int          i              = 0;

   @Override
   public SimpleEntry<String, String> createKey() {

      i++;
      return new SimpleEntry<>(KEY_PREF + i, ENCRYPTED_PREF + i);
   }

   @Override
   public String decryptKey(String encrypedKey) {

      return KEY_PREF + encrypedKey.substring(encrypedKey.length() - 1);
   }

}
