package com.mycodefu.werekitten.files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileStorage {
    public static final String USER_HOME = System.getProperty("user.home");
    public static final Path WEREKITTEN_HOME = Paths.get(USER_HOME, "WereKitten");
    public static final Path WEREKITTEN_HOME_LEVELS = WEREKITTEN_HOME.resolve("Levels");
    public static final String LEVEL_EXTENSION = ".wkl";

    public static boolean levelFileExists(String levelName){
        Path path = getLevelFilePath(levelName);
        return Files.exists(path);
    }

    public static void writeLevelFile(String levelName, String data) throws IOException {
        Path path = getLevelFilePath(levelName);
        ensureDirectoriesForFile(path);
        Files.write(path, data.getBytes(StandardCharsets.UTF_8));
    }
    public static String readLevelFile(String levelName) throws IOException {
        Path path = getLevelFilePath(levelName);
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public static Path getLevelFilePath(String levelName) {
        return WEREKITTEN_HOME_LEVELS.resolve(levelName).resolve(levelName + LEVEL_EXTENSION);
    }

    public static void ensureDirectoriesForFile(Path filePath) throws IOException {
        Path parent = filePath.getParent();
        Files.createDirectories(parent);
    }

}
