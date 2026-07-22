package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;

public class AutoFish extends Module {

    private final NumberSetting recastDelay = new NumberSetting("RecastDelay", 20.0, 5.0, 60.0, 1.0);

    private int cooldown;

    public AutoFish() {
        super("AutoFish", "Reels in and recasts your fishing rod automatically", Category.PLAYER);
        addSetting(recastDelay);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.playerController == null || mc.currentScreen != null) {
            return;
        }
        if (mc.player.getHeldItemMainhand().getItem() != Items.FISHING_ROD) {
            return;
        }
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        EntityFishHook bobber = mc.player.fishEntity;
        if (bobber == null) {
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
            cooldown = recastDelay.getValue().intValue();
            return;
        }
        if (bobber.isInWater() && bobber.motionY < -0.05) {
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
            cooldown = recastDelay.getValue().intValue();
        }
    }
}
