package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module {

    public AutoRespawn() {
        super("AutoRespawn", "Respawns instantly after death", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        if (mc.player.getHealth() <= 0.0F || mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }
}
