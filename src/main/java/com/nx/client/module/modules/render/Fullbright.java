package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;

public class Fullbright extends Module {

    private float previousGamma = 1.0F;

    public Fullbright() {
        super("Fullbright", "Removes darkness so you always see clearly", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.gameSettings != null) {
            previousGamma = mc.gameSettings.gammaSetting;
        }
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            mc.gameSettings.gammaSetting = previousGamma;
        }
    }

    @Override
    public void onTick() {
        if (mc.gameSettings != null) {
            mc.gameSettings.gammaSetting = 100.0F;
        }
    }
}
