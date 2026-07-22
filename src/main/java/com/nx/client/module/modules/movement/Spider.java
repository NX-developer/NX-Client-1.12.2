package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;

public class Spider extends Module {

    public Spider() {
        super("Spider", "Climb up walls like a spider", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        boolean moving = mc.player.movementInput.moveForward != 0.0F || mc.player.movementInput.moveStrafe != 0.0F;
        if (mc.player.collidedHorizontally && moving) {
            mc.player.motionY = 0.2;
        }
    }
}
