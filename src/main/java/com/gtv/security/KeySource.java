package com.gtv.security;

import java.util.AbstractMap.SimpleEntry;

public interface KeySource {

   SimpleEntry<byte[], byte[]> createKey();

   byte[] decryptKey(byte[] encryptedKey);

}
