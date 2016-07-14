package com.gtv.security;

public class EncryptionEnvelopeResult<P> {

   private boolean success;
   private String  message;
   private P       payload;

   public EncryptionEnvelopeResult(boolean success, String message, P payload) {
      super();
      this.success = success;
      this.message = message;
      this.payload = payload;
   }

   public boolean isSuccess() {

      return success;
   }

   public String getMessage() {

      return message;
   }

   public P getPayload() {

      return payload;
   }

}
