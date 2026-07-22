package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", "Removes the delay between block placements", Category.PLAYER);
    }

    @Override
    public void onTick() {
        try {
            ReflectionHelper.setPrivateValue(Minecraft.class, mc, Integer.valueOf(0),
                    "rightClickDelayTimer", "field_71467_ac");
        } catch (Throwable ignored) {
        }
    }
}
