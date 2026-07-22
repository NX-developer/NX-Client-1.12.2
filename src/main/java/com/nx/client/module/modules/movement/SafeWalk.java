package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;

public class SafeWalk extends Module {

    public SafeWalk() {
        super("SafeWalk", "Stops you from walking off ledges", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.gameSettings == null) {
            return;
        }
        if (!mc.player.onGround || mc.player.capabilities.isFlying) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            return;
        }
        BlockPos below = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        boolean edge = mc.world.getBlockState(below).getMaterial() == Material.AIR;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), edge);
    }
}
