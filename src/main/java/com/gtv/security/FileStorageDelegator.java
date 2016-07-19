package com.gtv.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FileStorageDelegator implements JpaDelegate {

   private static final String DATA_EXT         = "txt";
   private static final String KEY_EXT          = "key";
   private static final String KEY_ID_EXT       = "kid";
   private static String       UTF8             = "UTF8";
   public static final String  TEMP_DIR_KEYWORD = "tmp";

   private Path rootDir;

   @Value("${envelopeencrypter.jpa.file.rootdir}")
   private String rootDirName;

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
         Path file = Paths.get(rootDir.toString(), name);
         Files.write(file, content);
      }
      catch (Exception e) {
         throw new EncryptionEnvelopeException(e);
      }

   }

   @PostConstruct
   private void springInit() throws IOException {

      this.setRootDir(rootDirName);
   }

   @Override
   public SimpleEntry<String, byte[]> fetchData(String id) {

      byte[] value = fetch(rootDir, clean(id, DATA_EXT));
      byte[] keyId = fetch(rootDir, clean(id, KEY_ID_EXT));

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

      byte[] key = fetch(rootDir, clean(keyId, KEY_EXT));
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

   public void setRootDir(String folder) throws IOException {

      this.rootDirName = folder;
      if (rootDirName.equals(TEMP_DIR_KEYWORD)) {
         rootDir = Files.createTempDirectory(null);
         rootDir.toFile().deleteOnExit();
      } else {
         rootDir = Paths.get(rootDirName);
      }
   }

}
