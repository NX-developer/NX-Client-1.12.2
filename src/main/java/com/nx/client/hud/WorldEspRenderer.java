package com.nx.client.hud;

import com.nx.client.NXClient;
import com.nx.client.module.modules.render.BedEsp;
import com.nx.client.module.modules.render.ChestEsp;
import com.nx.client.module.modules.render.EntityEsp;
import com.nx.client.module.modules.render.ItemEsp;
import com.nx.client.module.modules.render.Nametags;
import com.nx.client.module.modules.render.Tracers;
import com.nx.client.module.modules.render.XRay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class WorldEspRenderer {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) {
            return;
        }
        XRay xray = NXClient.moduleManager.get(XRay.class);
        ChestEsp chestEsp = NXClient.moduleManager.get(ChestEsp.class);
        BedEsp bedEsp = NXClient.moduleManager.get(BedEsp.class);
        EntityEsp entityEsp = NXClient.moduleManager.get(EntityEsp.class);
        ItemEsp itemEsp = NXClient.moduleManager.get(ItemEsp.class);
        Tracers tracers = NXClient.moduleManager.get(Tracers.class);
        Nametags nametags = NXClient.moduleManager.get(Nametags.class);

        boolean xrayOn = xray != null && xray.isEnabled();
        boolean chestOn = chestEsp != null && chestEsp.isEnabled();
        boolean bedOn = bedEsp != null && bedEsp.isEnabled();
        boolean entityOn = entityEsp != null && entityEsp.isEnabled();
        boolean itemOn = itemEsp != null && itemEsp.isEnabled();
        boolean tracersOn = tracers != null && tracers.isEnabled();
        boolean nametagsOn = nametags != null && nametags.isEnabled();

        if (!xrayOn && !chestOn && !bedOn && !entityOn && !itemOn && !tracersOn && !nametagsOn) {
            return;
        }

        float partialTicks = event.getPartialTicks();
        RenderManager manager = mc.getRenderManager();
        double camX = manager.viewerPosX;
        double camY = manager.viewerPosY;
        double camZ = manager.viewerPosZ;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GL11.glLineWidth(1.6F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        if (xrayOn) {
            for (XRay.Ore ore : xray.getOres()) {
                drawBlockBox(buffer, ore.pos, camX, camY, camZ, ore.red, ore.green, ore.blue);
            }
        }
        if (chestOn) {
            for (BlockPos pos : chestEsp.getContainers()) {
                drawBlockBox(buffer, pos, camX, camY, camZ, 1.0F, 0.75F, 0.20F);
            }
        }
        if (bedOn) {
            for (BlockPos pos : bedEsp.getBeds()) {
                drawBlockBox(buffer, pos, camX, camY, camZ, 1.0F, 0.30F, 0.60F);
            }
        }
        if (itemOn) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityItem)) {
                    continue;
                }
                drawEntityBox(buffer, entity, camX, camY, camZ, partialTicks, 0.35F, 1.0F, 0.55F);
            }
        }
        if (entityOn || tracersOn) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityLivingBase) || entity == mc.player) {
                    continue;
                }
                EntityLivingBase living = (EntityLivingBase) entity;
                float red = 0.62F;
                float green = 0.30F;
                float blue = 0.87F;
                if (living instanceof IMob) {
                    red = 1.0F;
                    green = 0.25F;
                    blue = 0.35F;
                } else if (living instanceof EntityPlayer) {
                    red = 0.20F;
                    green = 0.85F;
                    blue = 1.0F;
                }
                if (entityOn && shouldShow(entityEsp, living)) {
                    drawEntityBox(buffer, living, camX, camY, camZ, partialTicks, red, green, blue);
                }
                if (tracersOn) {
                    drawTracer(buffer, living, camX, camY, camZ, partialTicks, red, green, blue);
                }
            }
        }

        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();

        if (nametagsOn) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityLivingBase) || entity == mc.player) {
                    continue;
                }
                EntityLivingBase living = (EntityLivingBase) entity;
                if (living instanceof EntityPlayer) {
                    if (!nametags.showPlayers()) {
                        continue;
                    }
                } else if (!nametags.showMobs()) {
                    continue;
                }
                drawNametag(mc, manager, living, camX, camY, camZ, partialTicks, nametags);
            }
        }
    }

    private boolean shouldShow(EntityEsp esp, EntityLivingBase living) {
        if (living instanceof IMob) {
            return esp.showHostile();
        }
        if (living instanceof EntityPlayer) {
            return esp.showPlayers();
        }
        return esp.showPassive();
    }

    private void drawBlockBox(BufferBuilder buffer, BlockPos pos, double camX, double camY, double camZ,
                              float red, float green, float blue) {
        double x1 = pos.getX() - camX;
        double y1 = pos.getY() - camY;
        double z1 = pos.getZ() - camZ;
        drawBox(buffer, x1, y1, z1, x1 + 1.0, y1 + 1.0, z1 + 1.0, red, green, blue);
    }

    private void drawEntityBox(BufferBuilder buffer, Entity entity, double camX, double camY, double camZ,
                               float partialTicks, float red, float green, float blue) {
        double x = interpolate(entity.lastTickPosX, entity.posX, partialTicks);
        double y = interpolate(entity.lastTickPosY, entity.posY, partialTicks);
        double z = interpolate(entity.lastTickPosZ, entity.posZ, partialTicks);
        AxisAlignedBB box = entity.getEntityBoundingBox();
        double width = (box.maxX - box.minX) / 2.0;
        double height = box.maxY - box.minY;
        drawBox(buffer,
                x - width - camX, y - camY, z - width - camZ,
                x + width - camX, y + height - camY, z + width - camZ,
                red, green, blue);
    }

    private void drawTracer(BufferBuilder buffer, Entity entity, double camX, double camY, double camZ,
                            float partialTicks, float red, float green, float blue) {
        Minecraft mc = Minecraft.getMinecraft();
        double x = interpolate(entity.lastTickPosX, entity.posX, partialTicks) - camX;
        double y = interpolate(entity.lastTickPosY, entity.posY, partialTicks) - camY + entity.height / 2.0;
        double z = interpolate(entity.lastTickPosZ, entity.posZ, partialTicks) - camZ;
        float yaw = (float) Math.toRadians(mc.player.rotationYaw);
        float pitch = (float) Math.toRadians(mc.player.rotationPitch);
        double startX = -Math.sin(yaw) * Math.cos(pitch) * 1.2;
        double startY = -Math.sin(pitch) * 1.2 + mc.player.getEyeHeight() - 0.2;
        double startZ = Math.cos(yaw) * Math.cos(pitch) * 1.2;
        buffer.pos(startX, startY, startZ).color(red, green, blue, 1.0F).endVertex();
        buffer.pos(x, y, z).color(red, green, blue, 1.0F).endVertex();
    }

    private void drawBox(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2,
                         float red, float green, float blue) {
        float alpha = 1.0F;
        line(buffer, x1, y1, z1, x2, y1, z1, red, green, blue, alpha);
        line(buffer, x2, y1, z1, x2, y1, z2, red, green, blue, alpha);
        line(buffer, x2, y1, z2, x1, y1, z2, red, green, blue, alpha);
        line(buffer, x1, y1, z2, x1, y1, z1, red, green, blue, alpha);
        line(buffer, x1, y2, z1, x2, y2, z1, red, green, blue, alpha);
        line(buffer, x2, y2, z1, x2, y2, z2, red, green, blue, alpha);
        line(buffer, x2, y2, z2, x1, y2, z2, red, green, blue, alpha);
        line(buffer, x1, y2, z2, x1, y2, z1, red, green, blue, alpha);
        line(buffer, x1, y1, z1, x1, y2, z1, red, green, blue, alpha);
        line(buffer, x2, y1, z1, x2, y2, z1, red, green, blue, alpha);
        line(buffer, x2, y1, z2, x2, y2, z2, red, green, blue, alpha);
        line(buffer, x1, y1, z2, x1, y2, z2, red, green, blue, alpha);
    }

    private void line(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2,
                      float red, float green, float blue, float alpha) {
        buffer.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
    }

    private void drawNametag(Minecraft mc, RenderManager manager, EntityLivingBase living,
                             double camX, double camY, double camZ, float partialTicks, Nametags module) {
        double x = interpolate(living.lastTickPosX, living.posX, partialTicks) - camX;
        double y = interpolate(living.lastTickPosY, living.posY, partialTicks) - camY + living.height + 0.5;
        double z = interpolate(living.lastTickPosZ, living.posZ, partialTicks) - camZ;

        String text = living.getName();
        if (module.showHealth()) {
            text = text + " " + (int) living.getHealth();
        }
        int color = living instanceof EntityPlayer ? 0xFF35D9FF : 0xFFF3E9FF;
        float scale = (float) (0.025 * module.getScale());

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-manager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(manager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        int width = mc.fontRenderer.getStringWidth(text) / 2;
        mc.fontRenderer.drawStringWithShadow(text, -width, 0, color);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private double interpolate(double last, double current, float partialTicks) {
        return last + (current - last) * partialTicks;
    }
}
