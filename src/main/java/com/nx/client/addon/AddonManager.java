package com.nx.client.addon;

import com.nx.client.NXClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

public class AddonManager {

    private final List<LoadedAddon> addons = new ArrayList<LoadedAddon>();

    public void loadAll(ASMDataTable asmData) {
        if (asmData == null) {
            return;
        }
        Set<ASMDataTable.ASMData> entries = asmData.getAll(NXAddonEntry.class.getName());
        for (ASMDataTable.ASMData entry : entries) {
            String name = entry.getClassName();
            String author = "Unknown";
            String version = "1.0.0";
            Map<String, Object> info = entry.getAnnotationInfo();
            if (info != null) {
                if (info.get("name") != null) {
                    name = String.valueOf(info.get("name"));
                }
                if (info.get("author") != null) {
                    author = String.valueOf(info.get("author"));
                }
                if (info.get("version") != null) {
                    version = String.valueOf(info.get("version"));
                }
            }
            int before = NXClient.moduleManager.getModules().size();
            boolean failed = false;
            try {
                Class<?> clazz = Class.forName(entry.getClassName());
                Object instance = clazz.newInstance();
                if (instance instanceof NXAddon) {
                    ((NXAddon) instance).onInitialize();
                } else {
                    failed = true;
                }
            } catch (Throwable throwable) {
                failed = true;
            }
            int added = NXClient.moduleManager.getModules().size() - before;
            addons.add(new LoadedAddon(name, author, version, added, failed));
        }
    }

    public List<LoadedAddon> getAddons() {
        return addons;
    }
}
