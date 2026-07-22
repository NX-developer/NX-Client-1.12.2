package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 3.0, 0.0, 20.0, 1.0);

    private int timer;

    public ChestStealer() {
        super("ChestStealer", "Takes every item out of an opened container", Category.PLAYER);
        addSetting(delay);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.playerController == null || mc.currentScreen == null) {
            return;
        }
        if (!(mc.player.openContainer instanceof ContainerChest)) {
            return;
        }
        if (timer > 0) {
            timer--;
            return;
        }
        ContainerChest container = (ContainerChest) mc.player.openContainer;
        int containerSlots = container.getLowerChestInventory().getSizeInventory();
        for (int i = 0; i < containerSlots; i++) {
            if (container.getSlot(i).getStack().isEmpty()) {
                continue;
            }
            mc.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
            timer = delay.getValue().intValue();
            return;
        }
    }
}
