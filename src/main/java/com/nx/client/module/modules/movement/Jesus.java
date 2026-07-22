package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;

public class Jesus extends Module {

    public Jesus() {
        super("Jesus", "Walk flat on top of water without bouncing", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (mc.player.isSneaking() || mc.player.capabilities.isFlying) {
            return;
        }
        BlockPos feet = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        boolean overWater = mc.world.getBlockState(feet).getMaterial() == Material.WATER
                || mc.world.getBlockState(feet.down()).getMaterial() == Material.WATER;
        if (overWater && mc.player.motionY <= 0.0) {
            mc.player.motionY = 0.0;
            mc.player.onGround = true;
        }
    }
}
