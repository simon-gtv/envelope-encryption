package com.gtv.security.stub;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gtv.security.JpaDelegate;

@Repository
public class JpaStub implements JpaDelegate {

	private Map<String, String> dataMap = new HashMap<>();

	@Override
	public String fetch(String key) {

		return dataMap.get(key);
	}

	@Override
	public void save(String key, String value) {

		dataMap.put(key, value);
	}

}
