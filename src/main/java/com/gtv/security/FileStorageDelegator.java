package com.gtv.security;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;

import org.springframework.stereotype.Component;

@Component
public class FileStorageDelegator implements JpaDelegate {

   private static final String DATA_EXT   = "txt";
   private static final String KEY_EXT    = "key";
   private static final String KEY_ID_EXT = "kid";
   private static String       UTF8       = "UTF8";

   private Path folder;

   private String clean(String name, String extn) {

      return name.replace(' ', '_') + "." + extn;
   }

   private byte[] fetch(Path folder, String name) {

      try {
         if (Files.isDirectory(folder)) {
            Path file = Paths.get(folder.toString(), name);
            return Files.readAllBytes(file);
         }
      }
      catch (NoSuchFileException e) {
         return null;
      }
      catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }
      throw new EncryptionEnvelopeException("Cannot access folder: " + folder);
   }

   private void save(String name, byte[] content) {

      try {
         Path file = Paths.get(folder.toString(), name);
         Files.write(file, content);
      }
      catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }

   }

   @Override
   public SimpleEntry<String, byte[]> fetchData(String id) {

      byte[] value = fetch(folder, clean(id, DATA_EXT));
      byte[] keyId = fetch(folder, clean(id, KEY_ID_EXT));

      if (keyId == null && value == null)
         return null;

      try {
         return new SimpleEntry<String, byte[]>(new String(keyId, UTF8), value);
      }
      catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }
   }

   @Override
   public byte[] fetchKey(String keyId) {

      byte[] key = fetch(folder, clean(keyId, KEY_EXT));
      return key == null ? null : key;
   }

   @Override
   public void saveData(String id, byte[] value, String keyId) {

      save(clean(id, DATA_EXT), value);
      try {
         save(clean(id, KEY_ID_EXT), keyId.getBytes(UTF8));
      }
      catch (Exception e) {
         // TODO: handle exception
      }
   }

   @Override
   public void saveKey(String keyName, byte[] value) {

      save(clean(keyName, KEY_EXT), value);
   }

   public void setFolder(Path folder) {

      this.folder = folder;
   }

}
