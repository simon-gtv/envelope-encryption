package com.gtv.security;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringIntegrationTest extends BaseTest {

   private static AnnotationConfigApplicationContext ctx;

   @AfterClass
   public static void close() {

      ctx.close();
   }

   @BeforeClass
   public static void init() {

      AppPropertysource props = new AppPropertysource();
      props.properties = new HashMap<String, Object>();
      ctx = new AnnotationConfigApplicationContext();
      ctx.getEnvironment().getPropertySources().addLast(props);

      props.properties.put("envelopeencrypter.aws.accesskey", "");
      props.properties.put("envelopeencrypter.aws.secretkey", "");
      props.properties.put("envelopeencrypter.aws.endpoint", "https://kms.eu-west-1.amazonaws.com");
      props.properties.put("envelopeencrypter.jpa.file.rootdir", FileStorageDelegator.TEMP_DIR_KEYWORD);
      ctx.scan("com.gtv.security");
      ctx.refresh();
      ee = ctx.getBean(EncryptionEnvelope.class);
   }

}
