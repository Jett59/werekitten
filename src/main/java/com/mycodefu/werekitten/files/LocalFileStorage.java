package com.mycodefu.werekitten.files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileStorage {
    private static final String USER_HOME = System.getProperty("user.home");
    private static final Path WEREKITTEN_HOME = Paths.get(USER_HOME, "WereKitten");
    private static final Path WEREKITTEN_HOME_LEVELS = WEREKITTEN_HOME.resolve("Levels");
    private static final String LEVEL_EXTENSION = ".wkl";

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
