package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class XRay extends Module {

    public static final class Ore {
        public final BlockPos pos;
        public final float red;
        public final float green;
        public final float blue;

        public Ore(BlockPos pos, float red, float green, float blue) {
            this.pos = pos;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }

    private static final Map<Block, float[]> ORE_COLORS = new HashMap<Block, float[]>();

    static {
        ORE_COLORS.put(Blocks.DIAMOND_ORE, new float[]{0.29F, 0.85F, 0.86F});
        ORE_COLORS.put(Blocks.EMERALD_ORE, new float[]{0.20F, 0.85F, 0.36F});
        ORE_COLORS.put(Blocks.GOLD_ORE, new float[]{1.0F, 0.84F, 0.0F});
        ORE_COLORS.put(Blocks.IRON_ORE, new float[]{0.86F, 0.74F, 0.62F});
        ORE_COLORS.put(Blocks.REDSTONE_ORE, new float[]{1.0F, 0.15F, 0.15F});
        ORE_COLORS.put(Blocks.LIT_REDSTONE_ORE, new float[]{1.0F, 0.15F, 0.15F});
        ORE_COLORS.put(Blocks.LAPIS_ORE, new float[]{0.20F, 0.35F, 0.90F});
        ORE_COLORS.put(Blocks.COAL_ORE, new float[]{0.25F, 0.25F, 0.25F});
        ORE_COLORS.put(Blocks.QUARTZ_ORE, new float[]{0.95F, 0.90F, 0.85F});
    }

    private final NumberSetting radius = new NumberSetting("Radius", 16.0, 4.0, 32.0, 1.0);

    private final List<Ore> ores = new ArrayList<Ore>();
    private int rescanTimer;

    public XRay() {
        super("XRay", "Highlights ores through walls", Category.RENDER);
        addSetting(radius);
    }

    @Override
    public void onEnable() {
        rescanTimer = 0;
        scan();
    }

    @Override
    public void onDisable() {
        synchronized (ores) {
            ores.clear();
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
        List<Ore> found = new ArrayList<Ore>();
        int r = radius.getValue().intValue();
        BlockPos origin = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = origin.add(x, y, z);
                    Block block = mc.world.getBlockState(pos).getBlock();
                    float[] color = ORE_COLORS.get(block);
                    if (color != null) {
                        found.add(new Ore(pos, color[0], color[1], color[2]));
                    }
                }
            }
        }
        synchronized (ores) {
            ores.clear();
            ores.addAll(found);
        }
    }

    public List<Ore> getOres() {
        synchronized (ores) {
            return new ArrayList<Ore>(ores);
        }
    }
}
