package com.gtv.security;

import static com.gtv.security.EncryptionEnvelope.toByteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public abstract class BaseTest {

   static final String ID0     = "not-there";
   static final String ID1     = "my-secrect-1";
   static final String ID2     = "my-secrect-2";
   static final String ID3     = "my-secrect-3";
   static final String KEY_ID1 = "1st-Key";
   static final String KEY_ID2 = "2nd-Key";
   static final byte[] SECRET1 = toByteArray("My Secret is ...");
   static final byte[] SECRET2 = toByteArray("The quick brown fox");
   static final byte[] SECRET3 = toByteArray("Jumps over the lazy dogs");

   protected static EncryptionEnvelope ee;

   @Test
   public void testBasicApi() {

      System.out.println("EE = " + ee);
      ee.store(ID1, SECRET1, KEY_ID1);
      byte[] data = ee.fetch(ID1);
      assertArrayEquals(SECRET1, data);

      ee.store(ID2, SECRET2, KEY_ID2);
      data = ee.fetch(ID2);
      assertArrayEquals(SECRET2, data);

      ee.store(ID3, SECRET3, KEY_ID2);
      data = ee.fetch(ID3);
      assertArrayEquals(SECRET3, data);
   }

   @Test
   public void testFailure() {

      System.out.println("EE = " + ee);
      try {
         ee.store(ID1, null, KEY_ID1);
         fail();
      }
      catch (Exception e) {
         assertTrue(e instanceof EncryptionEnvelopeException);
      }
   }

   @Test
   public void testNotFound() {

      System.out.println("EE = " + ee);
      byte[] data = ee.fetch(ID0);
      assertNull(data);
   }

}
