package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import net.minecraft.entity.item.EntityArmorStand;
import org.lwjgl.input.Keyboard;

public class Freecam extends Module {

    private final NumberSetting speed = new NumberSetting("Speed", 1.0, 0.2, 5.0, 0.1);

    private EntityArmorStand camera;
    private double playerX;
    private double playerY;
    private double playerZ;

    public Freecam() {
        super("Freecam", "Detaches the camera so you can fly around freely", Category.RENDER, Keyboard.KEY_V);
        addSetting(speed);
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        playerX = mc.player.posX;
        playerY = mc.player.posY;
        playerZ = mc.player.posZ;
        camera = new EntityArmorStand(mc.world);
        camera.setPosition(playerX, playerY, playerZ);
        camera.rotationYaw = mc.player.rotationYaw;
        camera.rotationPitch = mc.player.rotationPitch;
        camera.setInvisible(true);
        camera.setNoGravity(true);
        camera.noClip = true;
        mc.setRenderViewEntity(camera);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.setRenderViewEntity(mc.player);
            mc.player.setPosition(playerX, playerY, playerZ);
            mc.player.motionX = 0.0;
            mc.player.motionY = 0.0;
            mc.player.motionZ = 0.0;
        }
        camera = null;
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameSettings == null || camera == null) {
            return;
        }
        mc.player.setPosition(playerX, playerY, playerZ);
        mc.player.motionX = 0.0;
        mc.player.motionY = 0.0;
        mc.player.motionZ = 0.0;

        camera.prevRotationYaw = camera.rotationYaw;
        camera.prevRotationPitch = camera.rotationPitch;
        camera.rotationYaw = mc.player.rotationYaw;
        camera.rotationPitch = mc.player.rotationPitch;

        double factor = speed.getValue() * 0.5;
        double forward = 0.0;
        double strafe = 0.0;
        double vertical = 0.0;
        if (mc.gameSettings.keyBindForward.isKeyDown()) {
            forward += 1.0;
        }
        if (mc.gameSettings.keyBindBack.isKeyDown()) {
            forward -= 1.0;
        }
        if (mc.gameSettings.keyBindRight.isKeyDown()) {
            strafe += 1.0;
        }
        if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            strafe -= 1.0;
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            vertical += 1.0;
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            vertical -= 1.0;
        }

        double yaw = Math.toRadians(camera.rotationYaw);
        double sin = Math.sin(yaw);
        double cos = Math.cos(yaw);
        double dx = (strafe * cos + forward * sin) * factor;
        double dz = (forward * cos - strafe * sin) * factor;

        camera.prevPosX = camera.posX;
        camera.prevPosY = camera.posY;
        camera.prevPosZ = camera.posZ;
        camera.lastTickPosX = camera.posX;
        camera.lastTickPosY = camera.posY;
        camera.lastTickPosZ = camera.posZ;
        camera.setPosition(camera.posX + dx, camera.posY + vertical * factor, camera.posZ + dz);
    }
}
