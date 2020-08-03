package com.mycodefu.werekitten.level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodefu.werekitten.level.data.Level;

public class LevelSerializer {
private static ObjectMapper mapper = new ObjectMapper();

public static String serialize(Level level) {
	try {
	return mapper.writeValueAsString(level);
	}catch (Exception e) {
		throw new RuntimeException("error while serializing level "+level.getName(), e);
	}
}
}
