package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Cancels all fall damage by spoofing the on-ground flag", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.player.connection == null) {
            return;
        }
        if (mc.player.capabilities.isFlying) {
            return;
        }
        if (mc.player.fallDistance > 2.0F && mc.player.motionY < 0.0) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(
                    mc.player.posX, mc.player.posY, mc.player.posZ, true));
            mc.player.fallDistance = 0.0F;
        }
    }
}
