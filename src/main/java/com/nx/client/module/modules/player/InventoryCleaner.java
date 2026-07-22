package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryCleaner extends Module {

    private static final Set<Item> JUNK = new HashSet<Item>(Arrays.asList(
            Items.ROTTEN_FLESH,
            Items.POISONOUS_POTATO,
            Items.SPIDER_EYE,
            Items.FLINT,
            Items.STICK,
            Items.WHEAT_SEEDS,
            Item.getItemFromBlock(Blocks.DIRT),
            Item.getItemFromBlock(Blocks.COBBLESTONE),
            Item.getItemFromBlock(Blocks.GRAVEL)));

    private int timer;

    public InventoryCleaner() {
        super("InventoryCleaner", "Throws away junk items from your inventory", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.playerController == null || mc.currentScreen != null) {
            return;
        }
        if (timer > 0) {
            timer--;
            return;
        }
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty() || !JUNK.contains(stack.getItem())) {
                continue;
            }
            int screenSlot = i < 9 ? 36 + i : i;
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, screenSlot, 1,
                    ClickType.THROW, mc.player);
            timer = 4;
            return;
        }
    }
}
