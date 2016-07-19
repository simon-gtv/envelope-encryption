package com.gtv.security;

@SuppressWarnings("serial")
public class EncryptionEnvelopeException extends RuntimeException {

   public EncryptionEnvelopeException(Exception e) {
      super(e);
   }

   public EncryptionEnvelopeException(String msg) {
      super(msg);
   }

}
