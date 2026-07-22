package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;

public class BunnyHop extends Module {

    public BunnyHop() {
        super("BunnyHop", "Automatically jumps while moving forward", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        if (mc.player.onGround
                && mc.player.movementInput.moveForward != 0.0F
                && !mc.player.isSneaking()
                && !mc.player.isInWater()
                && !mc.player.capabilities.isFlying) {
            mc.player.motionY = 0.42;
        }
    }
}
