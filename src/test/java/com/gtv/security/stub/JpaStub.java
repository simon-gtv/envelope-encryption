package com.gtv.security.stub;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gtv.security.JpaDelegate;

@Repository
public class JpaStub implements JpaDelegate {

   public Map<String, String> dataKey = new HashMap<>();
   public Map<String, byte[]> dataMap = new HashMap<>();
   public Map<String, byte[]> keyMap  = new HashMap<>();

   private static void report(String string, String id, byte[] value) {

      System.out.println(string + " - " + id + ": " + Arrays.toString(value));
   }

   private static void report(String string, String id, String value) {

      System.out.println(string + " - " + id + ": " + value);
   }

   @Override
   public SimpleEntry<String, byte[]> fetchData(String id) {

      byte[] value = dataMap.get(id);
      report("get-id-val", id, value);
      if (value == null)
         return null;
      String key = dataKey.get(id);
      report("get-id-key", id, key);
      return new SimpleEntry<String, byte[]>(key, value);
   }

   @Override
   public byte[] fetchKey(String keyId) {

      byte[] key = keyMap.get(keyId);
      report("get-key", keyId, key);
      return key;
   }

   @Override
   public void saveData(String id, byte[] value, String keyId) {

      dataMap.put(id, value);
      report("save-id-val", id, value);
      dataKey.put(id, keyId);
      report("save-id-key", id, keyId);
   }

   @Override
   public void saveKey(String keyId, byte[] key) {

      keyMap.put(keyId, key);
      report("save-key", keyId, key);
   }

}
