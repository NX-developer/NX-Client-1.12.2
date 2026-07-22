package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class AutoTotem extends Module {

    private final NumberSetting health = new NumberSetting("Health", 12.0, 1.0, 20.0, 1.0);

    private int cooldown;

    public AutoTotem() {
        super("AutoTotem", "Moves a totem of undying into your offhand", Category.PLAYER);
        addSetting(health);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.playerController == null || mc.currentScreen != null) {
            return;
        }
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (mc.player.getHealth() > health.getValue().floatValue()) {
            return;
        }
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        int source = findTotem();
        if (source == -1) {
            return;
        }
        int windowId = mc.player.inventoryContainer.windowId;
        int sourceSlot = source < 9 ? 36 + source : source;
        mc.playerController.windowClick(windowId, sourceSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(windowId, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(windowId, sourceSlot, 0, ClickType.PICKUP, mc.player);
        cooldown = 10;
    }

    private int findTotem() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }
}
