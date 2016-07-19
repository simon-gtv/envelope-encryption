package com.gtv.security;

import java.nio.ByteBuffer;
import java.util.AbstractMap.SimpleEntry;

import org.springframework.stereotype.Component;

import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;

@Component
public class AwsKeySource implements KeySource {

   private static final String ALIAS    = "alias/envelope";
   private static final String KEY_TYPE = "AES_128";
   private final AWSKMSClient  kms;

   public AwsKeySource() {

      kms = new AWSKMSClient(awsCreds);

      // For more information, see http://amzn.to/1mKTMmG

      kms.setEndpoint("https://kms.eu-west-1.amazonaws.com");
   }

   @Override
   public SimpleEntry<byte[], byte[]> createKey() {

      byte[] plaintextKey = null;
      byte[] encryptedKey = null;
      GenerateDataKeyRequest dataKeyRequest = new GenerateDataKeyRequest();
      dataKeyRequest.setKeyId(ALIAS);
      dataKeyRequest.setKeySpec(KEY_TYPE);
      GenerateDataKeyResult dataKeyResult = kms.generateDataKey(dataKeyRequest);
      plaintextKey = dataKeyResult.getPlaintext().array();
      encryptedKey = dataKeyResult.getCiphertextBlob().array();
      return new SimpleEntry<>(plaintextKey, encryptedKey);
   }

   @Override
   public byte[] decryptKey(byte[] encryptedKey) {

      byte[] plainText = null;
      DecryptRequest req = new DecryptRequest();
      req.setCiphertextBlob(ByteBuffer.wrap(encryptedKey));
      DecryptResult result = kms.decrypt(req);
      plainText = result.getPlaintext().array();
      return plainText;
   }
}
