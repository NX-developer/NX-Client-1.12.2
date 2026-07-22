package com.nx.client.module.modules.combat;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class AutoClicker extends Module {

    private final NumberSetting cps = new NumberSetting("CPS", 10.0, 1.0, 20.0, 1.0);
    private long lastClick;

    public AutoClicker() {
        super("AutoClicker", "Automatically left clicks at a fixed rate while attacking", Category.COMBAT);
        addSetting(cps);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.playerController == null || mc.gameSettings == null) {
            return;
        }
        if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastClick < (long) (1000.0 / cps.getValue())) {
            return;
        }
        if (mc.objectMouseOver != null
                && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY
                && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            mc.playerController.attackEntity(mc.player, mc.objectMouseOver.entityHit);
        }
        mc.player.swingArm(EnumHand.MAIN_HAND);
        lastClick = now;
    }
}
