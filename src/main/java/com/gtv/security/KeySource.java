package com.gtv.security;

import java.util.AbstractMap.SimpleEntry;

public interface KeySource {

   SimpleEntry<String, String> createKey(String keyName);

   String decryptKey(String keyId);

}
