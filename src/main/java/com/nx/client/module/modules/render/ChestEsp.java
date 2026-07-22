package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.util.math.BlockPos;

public class ChestEsp extends Module {

    private final NumberSetting radius = new NumberSetting("Radius", 24.0, 8.0, 48.0, 1.0);

    private final List<BlockPos> containers = new ArrayList<BlockPos>();
    private int rescanTimer;

    public ChestEsp() {
        super("ChestESP", "Highlights nearby chests and containers", Category.RENDER);
        addSetting(radius);
    }

    @Override
    public void onEnable() {
        rescanTimer = 0;
        scan();
    }

    @Override
    public void onDisable() {
        synchronized (containers) {
            containers.clear();
        }
    }

    @Override
    public void onTick() {
        if (rescanTimer-- <= 0) {
            rescanTimer = 20;
            scan();
        }
    }

    private void scan() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        List<BlockPos> found = new ArrayList<BlockPos>();
        int r = radius.getValue().intValue();
        BlockPos origin = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = origin.add(x, y, z);
                    Block block = mc.world.getBlockState(pos).getBlock();
                    if (block instanceof BlockChest
                            || block instanceof BlockEnderChest
                            || block instanceof BlockShulkerBox) {
                        found.add(pos);
                    }
                }
            }
        }
        synchronized (containers) {
            containers.clear();
            containers.addAll(found);
        }
    }

    public List<BlockPos> getContainers() {
        synchronized (containers) {
            return new ArrayList<BlockPos>(containers);
        }
    }
}
