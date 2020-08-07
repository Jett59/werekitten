package com.mycodefu.werekitten.preferences;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.BackingStoreException;

public class Preferences {
    private static final String PREFERENCES_PATH = "/com/mycodefu/werekitten";

    public static final String CLIENT_CONNECT_IP_PREFERENCE = "CLIENT_CONNECT_IP";
    public static final String LISTENING_PORT_PREFERENCE = "LISTENING_PORT";
    public static final String DEFAULT_LEVEL_PREFERENCE = "DEFAULT_LEVEL";

    public static Map<String,String> loadPreferences() {
        Map<String,String> preferences = new ConcurrentHashMap<>();
        try {
            java.util.prefs.Preferences root = java.util.prefs.Preferences.userRoot();
            if (root.nodeExists(PREFERENCES_PATH)) {
                final java.util.prefs.Preferences node = root.node(PREFERENCES_PATH);
                Arrays.stream(node.keys()).forEach(key -> {
                    preferences.put(key, node.get(key, ""));
                });
            }
        } catch (BackingStoreException e) {
            System.out.printf("Unable to read preferences.\n%s", e);
        }
        return preferences;
    }

    public static void savePreferences(Map<String,String> preferences) {
        java.util.prefs.Preferences root = java.util.prefs.Preferences.userRoot();
        java.util.prefs.Preferences node = root.node(PREFERENCES_PATH);
        preferences.forEach(node::put);
    }
}
