package com.mycodefu.werekitten.level;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodefu.werekitten.files.LocalFileStorage;
import com.mycodefu.werekitten.level.data.Level;

public class LevelReader {
	public static String defaultLevelName = "default level";
	private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static Level read(String path) {
		try {
			Level level;
			if (LocalFileStorage.levelFileExists(defaultLevelName)) {
				level = mapper.readValue(LocalFileStorage.readLevelFile(defaultLevelName), Level.class);
			} else {
				level = mapper.readValue(LevelReader.class.getResourceAsStream(path), Level.class);
			}
			return level;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
