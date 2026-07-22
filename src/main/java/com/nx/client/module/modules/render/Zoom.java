package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import org.lwjgl.input.Keyboard;

public class Zoom extends Module {

    private float previousFov = 70.0F;

    public Zoom() {
        super("Zoom", "Zooms in your view while enabled", Category.RENDER, Keyboard.KEY_C);
    }

    @Override
    public void onEnable() {
        if (mc.gameSettings != null) {
            previousFov = mc.gameSettings.fovSetting;
        }
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            mc.gameSettings.fovSetting = previousFov;
        }
    }

    @Override
    public void onTick() {
        if (mc.gameSettings != null) {
            mc.gameSettings.fovSetting = 30.0F;
        }
    }
}
