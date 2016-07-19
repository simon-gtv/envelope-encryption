package com.gtv.security;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.gtv.security.stub.KeySourceStub;

public class FileStorageDelegatorTest extends BaseTest {

   private static final FileStorageDelegator FILE_JPA = new FileStorageDelegator();

   @AfterClass
   public static void close() {

      // ctx.close();
   }

   @BeforeClass
   public static void init() throws IOException {

      ee = new EncryptionEnvelope();
      FILE_JPA.setRootDir(FileStorageDelegator.TEMP_DIR_KEYWORD);
      ee.setJpaDelegate(FILE_JPA);
      ee.setKeySource(new KeySourceStub());
      System.out.println("EE = " + ee);
   }
}
