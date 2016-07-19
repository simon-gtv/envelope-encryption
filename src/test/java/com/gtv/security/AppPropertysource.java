package com.gtv.security;

import java.util.Map;

import org.springframework.core.env.PropertySource;

public class AppPropertysource extends PropertySource<String> {

   Map<String, Object> properties;

   AppPropertysource() {

      super("test");
   }

   @Override
   public Object getProperty(String name) {

      return properties.get(name);
   }

}
