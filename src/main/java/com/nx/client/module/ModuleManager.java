package com.nx.client.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<Module>();

    public void add(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getByCategory(Category category) {
        List<Module> result = new ArrayList<Module>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }

    public Module getByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public <T extends Module> T get(Class<T> clazz) {
        for (Module module : modules) {
            if (clazz.isInstance(module)) {
                return clazz.cast(module);
            }
        }
        return null;
    }

    public void onTick(Minecraft mc) {
        for (Module module : modules) {
            while (module.getKeyBinding().isPressed()) {
                module.toggle();
            }
        }
        if (mc.player == null || mc.world == null) {
            return;
        }
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
}
