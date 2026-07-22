package com.nx.client.module.modules.combat;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class Criticals extends Module {

    public Criticals() {
        super("Criticals", "Forces critical hits by faking a fall before every attack", Category.COMBAT);
    }

    public boolean shouldApply() {
        return isEnabled()
                && mc.player != null
                && mc.player.connection != null
                && mc.player.onGround
                && !mc.player.isInWater()
                && !mc.player.isInLava()
                && !mc.player.capabilities.isFlying;
    }

    public void fakeJump() {
        if (mc.player == null || mc.player.connection == null) {
            return;
        }
        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.0625, z, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 1.1E-5, z, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
    }
}
