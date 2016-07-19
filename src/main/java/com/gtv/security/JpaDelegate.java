package com.gtv.security;

import java.util.AbstractMap.SimpleEntry;

public interface JpaDelegate {

   SimpleEntry<String, byte[]> fetchData(String id);

   byte[] fetchKey(String keyId);

   void saveData(String id, byte[] value, String keyId);

   void saveKey(String keyName, byte[] data);
}
