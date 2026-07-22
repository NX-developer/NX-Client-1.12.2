package com.nx.client.module.modules.combat;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.BooleanSetting;
import com.nx.client.settings.NumberSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

public class KillAura extends Module {

    private final NumberSetting range = new NumberSetting("Range", 6.0, 3.0, 30.0, 0.5);
    private final NumberSetting cps = new NumberSetting("CPS", 12.0, 1.0, 20.0, 1.0);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting animals = new BooleanSetting("Animals", true);

    private long lastAttack;

    public KillAura() {
        super("KillAura", "Attacks every entity in range at once through walls", Category.COMBAT, Keyboard.KEY_R);
        addSetting(range);
        addSetting(cps);
        addSetting(players);
        addSetting(animals);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.playerController == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastAttack < (long) (1000.0 / cps.getValue())) {
            return;
        }
        List<EntityLivingBase> targets = collectTargets();
        if (targets.isEmpty()) {
            return;
        }
        for (EntityLivingBase target : targets) {
            mc.playerController.attackEntity(mc.player, target);
        }
        mc.player.swingArm(EnumHand.MAIN_HAND);
        lastAttack = now;
    }

    private List<EntityLivingBase> collectTargets() {
        List<EntityLivingBase> result = new ArrayList<EntityLivingBase>();
        double maxRange = range.getValue();
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase) || entity == mc.player) {
                continue;
            }
            EntityLivingBase living = (EntityLivingBase) entity;
            if (living.isDead || living.getHealth() <= 0.0F) {
                continue;
            }
            if (living instanceof EntityPlayer) {
                if (!players.isOn()) {
                    continue;
                }
            } else if (!(living instanceof IMob) && !animals.isOn()) {
                continue;
            }
            if (mc.player.getDistance(living) <= maxRange) {
                result.add(living);
            }
        }
        return result;
    }
}
