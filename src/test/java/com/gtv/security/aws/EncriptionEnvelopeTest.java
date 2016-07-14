package com.gtv.security.aws;

import org.junit.Test;

import com.gtv.security.EncriptionEnvelope;

public class EncriptionEnvelopeTest {

   private static EncriptionEnvelope testMe = new EncriptionEnvelope();

   @Test
   public void testSuccess() {
      
      testMe.createMasterKey(keyName)

   }

   @Test
   public void testNotFound() {

   }

   @Test
   public void testFailure() {

   }
}
