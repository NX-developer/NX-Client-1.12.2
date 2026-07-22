package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {

    private final NumberSetting speed = new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1);

    public Fly() {
        super("Fly", "Lets you fly freely in singleplayer", Category.MOVEMENT, Keyboard.KEY_F);
        addSetting(speed);
    }

    @Override
    public void onDisable() {
        if (mc.player != null && !mc.player.capabilities.isCreativeMode) {
            mc.player.capabilities.isFlying = false;
            mc.player.capabilities.allowFlying = false;
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        mc.player.capabilities.allowFlying = true;
        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.setFlySpeed((float) (0.05 * speed.getValue()));
        mc.player.motionY = 0.0;
    }
}
