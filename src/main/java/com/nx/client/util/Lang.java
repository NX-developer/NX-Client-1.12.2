package com.nx.client.util;

import net.minecraft.client.resources.I18n;

public final class Lang {

    private Lang() {
    }

    public static String get(String key, String fallback) {
        if (I18n.hasKey(key)) {
            return I18n.format(key);
        }
        return fallback;
    }

    public static String moduleDescription(String moduleName, String fallback) {
        return get("nxclient.module." + normalize(moduleName) + ".description", fallback);
    }

    public static String setting(String settingName) {
        return get("nxclient.setting." + normalize(settingName), settingName);
    }

    public static String category(String categoryName, String fallback) {
        return get("nxclient.category." + normalize(categoryName), fallback);
    }

    public static String gui(String id, String fallback) {
        return get("nxclient.gui." + id, fallback);
    }

    private static String normalize(String value) {
        return value.toLowerCase().replace(" ", "");
    }
}
