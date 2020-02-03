package com.mycodefu.werekitten.animation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.shape.Polygon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class WereKittenPolygon {
    private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String file;
    private Double[] polygon;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Double[] getPolygon() {
        return polygon;
    }

    public void setPolygon(Double[] polygon) {
        this.polygon = polygon;
    }

    public static void write(Polygon polygon, File file) {
        WereKittenPolygon wereKittenPolygon = new WereKittenPolygon();
        wereKittenPolygon.file = file.getName();
        wereKittenPolygon.polygon = polygon.getPoints().toArray(new Double[]{});
        try {
            mapper.writeValue(new File(file.getAbsolutePath().replace(".png", ".wkp")), wereKittenPolygon);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write WereKittenPolygon (wkp) file for polygon.");
        }
    }
    public static WereKittenPolygon read(String resourcePath){
        try {
            InputStream inputStream = WereKittenPolygon.class.getResourceAsStream(resourcePath);
            return mapper.readValue(inputStream, WereKittenPolygon.class);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to read WereKittenPolygon (wkp) resource file '%s'.", resourcePath));
        }
    }
}
