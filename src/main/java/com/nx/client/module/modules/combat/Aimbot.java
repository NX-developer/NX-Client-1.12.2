package com.nx.client.module.modules.combat;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.BooleanSetting;
import com.nx.client.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class Aimbot extends Module {

    private final NumberSetting range = new NumberSetting("Range", 6.0, 2.0, 20.0, 0.5);
    private final NumberSetting speed = new NumberSetting("Speed", 0.3, 0.05, 1.0, 0.05);
    private final BooleanSetting hostile = new BooleanSetting("Hostile", true);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting animals = new BooleanSetting("Animals", true);

    public Aimbot() {
        super("Aimbot", "Smoothly rotates your view toward the nearest target", Category.COMBAT, Keyboard.KEY_X);
        addSetting(range);
        addSetting(speed);
        addSetting(hostile);
        addSetting(players);
        addSetting(animals);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        EntityLivingBase target = findTarget();
        if (target == null) {
            return;
        }
        double dx = target.posX - mc.player.posX;
        double dz = target.posZ - mc.player.posZ;
        double dy = (target.posY + target.getEyeHeight()) - (mc.player.posY + mc.player.getEyeHeight());
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float targetPitch = (float) (-Math.toDegrees(Math.atan2(dy, horizontal)));
        float factor = speed.getValue().floatValue();
        float yawDelta = MathHelper.wrapDegrees(targetYaw - mc.player.rotationYaw);
        float pitchDelta = targetPitch - mc.player.rotationPitch;
        mc.player.rotationYaw += yawDelta * factor;
        mc.player.rotationPitch = MathHelper.clamp(mc.player.rotationPitch + pitchDelta * factor, -90.0F, 90.0F);
        mc.player.rotationYawHead = mc.player.rotationYaw;
    }

    private EntityLivingBase findTarget() {
        EntityLivingBase closest = null;
        double closestDist = range.getValue();
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase) || entity == mc.player) {
                continue;
            }
            EntityLivingBase living = (EntityLivingBase) entity;
            if (living.isDead || living.getHealth() <= 0.0F || !isAllowed(living)) {
                continue;
            }
            double dist = mc.player.getDistance(living);
            if (dist <= closestDist) {
                closest = living;
                closestDist = dist;
            }
        }
        return closest;
    }

    private boolean isAllowed(EntityLivingBase living) {
        if (living instanceof EntityPlayer) {
            return players.isOn();
        }
        if (living instanceof IMob) {
            return hostile.isOn();
        }
        return animals.isOn();
    }
}
