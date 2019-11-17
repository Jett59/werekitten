package com.mycodefu.werekitten.json;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodefu.werekitten.level.WereKittenLevel;

public class WereKittenLevelAssembler {
	private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static WereKittenLevel createLevel() {
		try {
			WereKittenLevel wereKittenLevel = mapper.readValue(WereKittenLevelAssembler.class.getResourceAsStream("/level.wkl"), WereKittenLevel.class);
			System.out.println(wereKittenLevel.getName());
			return wereKittenLevel;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
