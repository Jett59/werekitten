package com.mycodefu.werekitten.pipeline.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class PipelineConfig {
    private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    List<PipelineConfiguration> pipelines;

    public List<PipelineConfiguration> getPipelines() {
        return pipelines;
    }

    public static PipelineConfig read() {
        try {
            return mapper.readValue(PipelineConfig.class.getResourceAsStream("/pipeline-config.wkc"), PipelineConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
