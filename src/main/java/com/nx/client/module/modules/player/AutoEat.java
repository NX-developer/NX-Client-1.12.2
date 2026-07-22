package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class AutoEat extends Module {

    private final NumberSetting hunger = new NumberSetting("Hunger", 16.0, 1.0, 19.0, 1.0);

    private boolean eating;
    private int previousSlot = -1;

    public AutoEat() {
        super("AutoEat", "Automatically eats food when you get hungry", Category.PLAYER);
        addSetting(hunger);
    }

    @Override
    public void onDisable() {
        stopEating();
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameSettings == null || mc.currentScreen != null) {
            return;
        }
        int foodLevel = mc.player.getFoodStats().getFoodLevel();
        if (eating) {
            if (foodLevel >= 20 || !mc.player.isHandActive()) {
                stopEating();
            }
            return;
        }
        if (foodLevel > hunger.getValue().intValue()) {
            return;
        }
        int slot = findFood();
        if (slot == -1) {
            return;
        }
        previousSlot = mc.player.inventory.currentItem;
        mc.player.inventory.currentItem = slot;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        eating = true;
    }

    private void stopEating() {
        if (mc.gameSettings != null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        }
        if (eating && previousSlot != -1 && mc.player != null) {
            mc.player.inventory.currentItem = previousSlot;
        }
        previousSlot = -1;
        eating = false;
    }

    private int findFood() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemFood) {
                return i;
            }
        }
        return -1;
    }
}
