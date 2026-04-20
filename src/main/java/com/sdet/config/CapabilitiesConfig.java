package com.sdet.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads Appium device capabilities from capabilities.json.
 * Supports multiple device profiles (emulator, real-device, browserstack).
 */
public class CapabilitiesConfig {

    private static final Logger log = LoggerFactory.getLogger(CapabilitiesConfig.class);
    private static final String CONFIG_FILE = "capabilities.json";

    private CapabilitiesConfig() {}

    public static Map<String, Object> load(String profile) {
        try (InputStream is = CapabilitiesConfig.class
                .getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (is == null) {
                throw new RuntimeException("capabilities.json not found on classpath");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);
            JsonNode profileNode = root.get(profile);

            if (profileNode == null) {
                throw new RuntimeException("Profile '" + profile + "' not found in capabilities.json");
            }

            Map<String, Object> caps = new HashMap<>();
            profileNode.fields().forEachRemaining(entry ->
                caps.put(entry.getKey(), entry.getValue().asText())
            );

            log.info("Loaded capabilities profile: {}", profile);
            return caps;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load capabilities.json", e);
        }
    }
}
