package com.gtv.security.stub;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gtv.security.JpaDelegate;

@Repository
public class JpaStub implements JpaDelegate {

   public Map<String, String> dataMap = new HashMap<>();
   public Map<String, String> keyMap  = new HashMap<>();
   public Map<String, String> dataKey = new HashMap<>();

   @Override
   public void saveData(String id, String value, String keyId) {

      dataMap.put(id, value);
      dataKey.put(id, keyId);
   }

   @Override
   public SimpleEntry<String, String> fetchData(String id) {

      String value = dataMap.get(id);
      if (value == null)
         return null;
      String key = dataKey.get(id);
      return new SimpleEntry<String, String>(key, value);
   }

   @Override
   public String fetchKey(String keyId) {

      return keyMap.get(keyId);
   }

   @Override
   public void saveKey(String keyName, String value) {

      keyMap.put(keyName, value);
   }

}
