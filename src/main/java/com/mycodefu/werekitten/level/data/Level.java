package com.mycodefu.werekitten.level.data;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private String version;
    private String name;
    private String description;
    private Size size;
    private List<Layer> layers = new ArrayList<>();

    public Level() {

    }

    public Level(String version, String name, String description, List<Layer> layers) {
        setVersion(version);
        setName(name);
        setDescription(description);
        setLayers(layers);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public void addLayer(Layer lay) {
        layers.add(lay);
    }

    public void removeLayer(Layer lay) {
        layers.remove(lay);
    }

}
