package com.gtv.security;

import static com.gtv.security.EncryptionEnvelope.toByteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gtv.security.stub.JpaStub;
import com.gtv.security.stub.KeySourceStub;

public class EncryptionEnvelopeTest extends BaseTest {

   private static final String ID0     = "not-there";
   private static final String ID1     = "my-secrect-1";
   private static final String ID2     = "my-secrect-2";
   private static final String ID3     = "my-secrect-3";
   private static JpaStub      jpa     = new JpaStub();
   private static final String KEY_ID1 = "1st-Key";
   private static final String KEY_ID2 = "2nd-Key";
   private static final byte[] SECRET1 = toByteArray("My Secret is ...");
   private static final byte[] SECRET2 = toByteArray("The quick brown fox");
   private static final byte[] SECRET3 = toByteArray("Jumps over the lazy dogs");

   @BeforeClass
   public static void init() {

      ee = new EncryptionEnvelope();
      ee.setJpaDelegate(jpa);
      ee.setKeySource(new KeySourceStub());
   }

   @Test
   public void testWithExtraChecks() {

      KeySourceStub.i = 0;
      jpa.dataMap.clear();
      jpa.keyMap.clear();
      jpa.dataKey.clear();

      ee.store(ID1, SECRET1, KEY_ID1);
      byte[] data = ee.fetch(ID1);
      assertArrayEquals(SECRET1, data);
      assertEquals(1, jpa.dataMap.size());
      assertEquals(1, jpa.keyMap.size());
      assertEquals(1, jpa.dataKey.size());
      assertTrue(jpa.dataMap.keySet().contains(ID1));
      assertFalse(jpa.dataMap.values().contains(SECRET1));
      assertTrue(jpa.keyMap.keySet().contains(KEY_ID1));
      assertFalse(jpa.dataMap.values().contains(KeySourceStub.ENCRYPTED_PREF + 1));
      assertTrue(jpa.dataKey.keySet().contains(ID1));
      assertTrue(jpa.dataKey.values().contains(KEY_ID1));

      ee.store(ID2, SECRET2, KEY_ID2);
      data = ee.fetch(ID2);
      assertArrayEquals(SECRET2, data);
      assertEquals(2, jpa.dataMap.size());
      assertEquals(2, jpa.keyMap.size());
      assertEquals(2, jpa.dataKey.size());

      ee.store(ID3, SECRET3, KEY_ID2);
      data = ee.fetch(ID3);
      assertArrayEquals(SECRET3, data);
      assertEquals(3, jpa.dataMap.size());
      assertEquals(2, jpa.keyMap.size());
      assertEquals(3, jpa.dataKey.size());

   }
}
