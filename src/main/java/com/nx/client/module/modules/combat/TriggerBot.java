package com.nx.client.module.modules.combat;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class TriggerBot extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 80.0, 0.0, 500.0, 10.0);
    private long lastAttack;

    public TriggerBot() {
        super("TriggerBot", "Attacks whatever entity your crosshair is over", Category.COMBAT);
        addSetting(delay);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.playerController == null || mc.objectMouseOver == null) {
            return;
        }
        if (mc.objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY) {
            return;
        }
        if (!(mc.objectMouseOver.entityHit instanceof EntityLivingBase)) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastAttack < delay.getValue().longValue()) {
            return;
        }
        mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        lastAttack = now;
    }
}
