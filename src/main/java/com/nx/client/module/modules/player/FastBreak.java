package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FastBreak extends Module {

    public FastBreak() {
        super("FastBreak", "Removes the delay between breaking blocks", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (mc.playerController == null) {
            return;
        }
        try {
            ReflectionHelper.setPrivateValue(PlayerControllerMP.class, mc.playerController, Integer.valueOf(0),
                    "blockHitDelay", "field_78781_i");
        } catch (Throwable ignored) {
        }
    }
}
