package com.mycodefu.json;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.alibaba.fastjson.JSON;
import com.mycodefu.level.WereKittenLevel;

public class WereKittenLevelAssembler {
private static byte[] readWKL() {
	byte[] bytes;
	try {
		bytes = Files.readAllBytes(Paths.get("templates/level.wkl"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		bytes = null;
		e.printStackTrace();
	}
	return bytes;
}
private static String bytesToString(byte[] bytes) {
	return new String(bytes, Charset.forName("utf-8"));
}
public static WereKittenLevel createLevel() {
	String json = bytesToString(readWKL());
	return JSON.parseObject(json, WereKittenLevel.class);
}
}
