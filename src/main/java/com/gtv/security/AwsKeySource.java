package com.gtv.security;

import java.nio.ByteBuffer;
import java.util.AbstractMap.SimpleEntry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;

@Component
public class AwsKeySource implements KeySource {

   private static final String ALIAS    = "alias/envelope";
   private static final String KEY_TYPE = "AES_128";
   @Value("${envelopeencrypter.aws.accesskey}")
   private String              accessKey;
   @Value("${envelopeencrypter.aws.endpoint}")
   private String              endpoint;
   private AWSKMSClient        kms;
   @Value("${envelopeencrypter.aws.secretkey}")
   private String              secretKey;

   @PostConstruct
   private void init() {

      AWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
      kms = new AWSKMSClient(awsCreds);
      kms.setEndpoint(endpoint);
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
