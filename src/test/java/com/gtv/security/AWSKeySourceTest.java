package com.gtv.security;

import static com.gtv.security.EncryptionEnvelope.toByteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.BeforeClass;
import org.junit.Test;

public class AWSKeySourceTest {

   private static EncryptionEnvelope         EE       = new EncryptionEnvelope();
   private static final FileStorageDelegator FILE_JPA = new FileStorageDelegator();

   private static final String ID0     = "not-there";
   private static final String ID1     = "my-secrect-1";
   private static final String ID2     = "my-secrect-2";
   private static final String ID3     = "my-secrect-3";
   private static final String KEY_ID1 = "1st-Key";
   private static final String KEY_ID2 = "2nd-Key";
   private static final byte[] SECRET1 = toByteArray("My Secret is ...");
   private static final byte[] SECRET2 = toByteArray("The quick brown fox");
   private static final byte[] SECRET3 = toByteArray("Jumps over the lazy dogs");

   @BeforeClass
   public static void init() throws IOException {

      Path dir = Files.createTempDirectory(null);
      dir.toFile().deleteOnExit();
      EE.setJpaDelegate(FILE_JPA);
      FILE_JPA.setFolder(dir);
      EE.setKeySource(new AwsKeySource());
   }

   @Test
   public void testFailure() {

      try {
         EE.store(ID1, null, KEY_ID1);
         fail();
      }
      catch (Exception e) {
         assertTrue(e instanceof EncryptionEnvelopeException);
      }
   }

   @Test
   public void testNotFound() {

      byte[] data = EE.fetch(ID0);
      assertNull(data);
   }

   @Test
   public void testWithFileStorage() {

      EE.store(ID1, SECRET1, KEY_ID1);
      byte[] data = EE.fetch(ID1);
      assertArrayEquals(SECRET1, data);

      EE.store(ID2, SECRET2, KEY_ID2);
      data = EE.fetch(ID2);
      assertArrayEquals(SECRET2, data);

      EE.store(ID3, SECRET3, KEY_ID2);
      data = EE.fetch(ID3);
      assertArrayEquals(SECRET3, data);
   }

}
