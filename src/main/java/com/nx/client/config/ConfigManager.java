package com.nx.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nx.client.NXClient;
import com.nx.client.module.Module;
import com.nx.client.settings.BooleanSetting;
import com.nx.client.settings.NumberSetting;
import com.nx.client.settings.Setting;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;

public final class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ConfigManager() {
    }

    private static File getFile() {
        File directory = new File(Minecraft.getMinecraft().gameDir, "config");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new File(directory, "nxclient.json");
    }

    public static void save() {
        JsonObject root = new JsonObject();
        for (Module module : NXClient.moduleManager.getModules()) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enabled", module.isEnabled());
            JsonObject settingsObject = new JsonObject();
            for (Setting<?> setting : module.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    settingsObject.addProperty(setting.getName(), ((BooleanSetting) setting).isOn());
                } else if (setting instanceof NumberSetting) {
                    settingsObject.addProperty(setting.getName(), ((NumberSetting) setting).getValue());
                }
            }
            moduleObject.add("settings", settingsObject);
            root.add(module.getName(), moduleObject);
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(getFile());
            writer.write(GSON.toJson(root));
        } catch (Exception ignored) {
        } finally {
            close(writer);
        }
    }

    public static void load() {
        File file = getFile();
        if (!file.exists()) {
            return;
        }
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            JsonObject root = new JsonParser().parse(reader).getAsJsonObject();
            for (Module module : NXClient.moduleManager.getModules()) {
                if (!root.has(module.getName())) {
                    continue;
                }
                JsonObject moduleObject = root.getAsJsonObject(module.getName());
                if (moduleObject.has("enabled")) {
                    module.setEnabledSilently(moduleObject.get("enabled").getAsBoolean());
                }
                if (moduleObject.has("settings")) {
                    JsonObject settingsObject = moduleObject.getAsJsonObject("settings");
                    for (Setting<?> setting : module.getSettings()) {
                        if (!settingsObject.has(setting.getName())) {
                            continue;
                        }
                        if (setting instanceof BooleanSetting) {
                            ((BooleanSetting) setting).setValue(settingsObject.get(setting.getName()).getAsBoolean());
                        } else if (setting instanceof NumberSetting) {
                            ((NumberSetting) setting).setClamped(settingsObject.get(setting.getName()).getAsDouble());
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            close(reader);
        }
    }

    private static void close(java.io.Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
