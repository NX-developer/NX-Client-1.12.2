package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Nuker extends Module {

    private final NumberSetting range = new NumberSetting("Range", 4.0, 1.0, 6.0, 1.0);

    public Nuker() {
        super("Nuker", "Rapidly breaks all blocks around you", Category.PLAYER);
        addSetting(range);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.playerController == null) {
            return;
        }
        int radius = range.getValue().intValue();
        BlockPos origin = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        BlockPos target = null;
        double closest = Double.MAX_VALUE;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = origin.add(x, y, z);
                    if (mc.world.isAirBlock(pos)) {
                        continue;
                    }
                    if (mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
                        continue;
                    }
                    double dist = mc.player.getDistanceSq(pos);
                    if (dist < closest) {
                        closest = dist;
                        target = pos;
                    }
                }
            }
        }
        if (target != null) {
            mc.playerController.clickBlock(target, EnumFacing.UP);
            mc.playerController.onPlayerDamageBlock(target, EnumFacing.UP);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
}
