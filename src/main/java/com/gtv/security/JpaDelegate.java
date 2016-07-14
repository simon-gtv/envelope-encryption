package com.gtv.security;

import java.util.AbstractMap.SimpleEntry;

public interface JpaDelegate {

   void saveData(String id, String value, String keyId);

   SimpleEntry<String, String> fetchData(String id);

   String fetchKey(String keyId);

   void saveKey(String keyName, String value);
}
