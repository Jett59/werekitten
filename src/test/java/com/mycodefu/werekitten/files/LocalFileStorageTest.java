package com.mycodefu.werekitten.files;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileStorageTest {

    @Test
    void writeLevelFile() throws IOException {
        String data = "hello" + UUID.randomUUID();
        LocalFileStorage.writeLevelFile("luke", data);
        Path levelFilePath = LocalFileStorage.getLevelFilePath("luke");
        assertTrue(Files.exists(levelFilePath));
        String fileContents = Files.readString(levelFilePath);
        assertEquals(data, fileContents);
    }

    @Test
    void readLevelFile() throws IOException {
        String data = "hello" + UUID.randomUUID();
        Path levelFilePath = LocalFileStorage.getLevelFilePath("jett");
        LocalFileStorage.ensureDirectoriesForFile(levelFilePath);
        Files.write(levelFilePath, data.getBytes(StandardCharsets.UTF_8));

        String result = LocalFileStorage.readLevelFile("jett");
        assertEquals(data, result);
    }

    @Test
    void getLevelFilePath() {
        Path levelFilePath = LocalFileStorage.getLevelFilePath("luke");
        assertNotNull(levelFilePath);
        System.out.println(levelFilePath);
    }

    @Test
    void ensureDirectoriesForFile() throws IOException {
        Path levelFilePath = LocalFileStorage.getLevelFilePath("luke");
        LocalFileStorage.ensureDirectoriesForFile(levelFilePath);
    }
}