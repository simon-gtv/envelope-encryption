package com.gtv.security;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public interface JpaDelegate {

   static final String STORED    = "Stored";
   static final String NOT_FOUND = "Not found";

   EncryptionEnvelopeResult<Void> saveData(String id, String value, String keyId);

   EncryptionEnvelopeResult<SimpleEntry<String, String>> fetchData(String id);

   EncryptionEnvelopeResult<Entry<String, String>> fetchKey(String keyName);

   EncryptionEnvelopeResult<Entry<String, String>> saveKey(String keyName, String value);
}
