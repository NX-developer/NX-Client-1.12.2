package com.nx.client.module.modules.combat;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;

public class AntiKnockback extends Module {

    private final NumberSetting strength = new NumberSetting("Strength", 0.0, 0.0, 1.0, 0.05);

    public AntiKnockback() {
        super("AntiKnockback", "Reduces the knockback you take from hits", Category.COMBAT);
        addSetting(strength);
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        if (mc.player.hurtTime > 0) {
            mc.player.motionX *= strength.getValue();
            mc.player.motionZ *= strength.getValue();
        }
    }
}
