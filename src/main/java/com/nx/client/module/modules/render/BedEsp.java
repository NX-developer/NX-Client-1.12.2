package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockBed;
import net.minecraft.util.math.BlockPos;

public class BedEsp extends Module {

    private final NumberSetting radius = new NumberSetting("Radius", 32.0, 8.0, 64.0, 1.0);

    private final List<BlockPos> beds = new ArrayList<BlockPos>();
    private int rescanTimer;

    public BedEsp() {
        super("BedESP", "Highlights nearby beds through walls", Category.RENDER);
        addSetting(radius);
    }

    @Override
    public void onEnable() {
        rescanTimer = 0;
        scan();
    }

    @Override
    public void onDisable() {
        synchronized (beds) {
            beds.clear();
        }
    }

    @Override
    public void onTick() {
        if (rescanTimer-- <= 0) {
            rescanTimer = 40;
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
                    if (mc.world.getBlockState(pos).getBlock() instanceof BlockBed) {
                        found.add(pos);
                    }
                }
            }
        }
        synchronized (beds) {
            beds.clear();
            beds.addAll(found);
        }
    }

    public List<BlockPos> getBeds() {
        synchronized (beds) {
            return new ArrayList<BlockPos>(beds);
        }
    }
}
