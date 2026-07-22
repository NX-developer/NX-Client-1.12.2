package com.nx.client.hud;

import com.nx.client.NXClient;
import com.nx.client.gui.Theme;
import com.nx.client.module.Module;
import com.nx.client.module.modules.render.Coordinates;
import com.nx.client.module.modules.render.FpsCounter;
import com.nx.client.module.modules.render.HudModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudOverlay extends Gui {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.gameSettings.showDebugInfo) {
            return;
        }
        HudModule hud = NXClient.moduleManager.get(HudModule.class);
        if (hud == null || !hud.isEnabled()) {
            return;
        }
        ScaledResolution resolution = new ScaledResolution(mc);
        drawWatermark(mc);
        drawArrayList(mc, resolution.getScaledWidth());
        drawInfo(mc, resolution.getScaledHeight());
    }

    private void drawWatermark(Minecraft mc) {
        int nxWidth = mc.fontRenderer.getStringWidth("NX");
        int total = nxWidth + mc.fontRenderer.getStringWidth("CLIENT") + 16;
        drawRect(4, 4, 4 + total, 18, 0xB0140B22);
        drawRect(4, 4, 6, 18, Theme.ACCENT);
        mc.fontRenderer.drawStringWithShadow("NX", 11, 8, Theme.ACCENT);
        mc.fontRenderer.drawStringWithShadow("CLIENT", 13 + nxWidth, 8, Theme.TEXT);
    }

    private void drawArrayList(Minecraft mc, int screenWidth) {
        List<Module> active = new ArrayList<Module>();
        for (Module module : NXClient.moduleManager.getModules()) {
            if (module.isEnabled() && !(module instanceof HudModule)) {
                active.add(module);
            }
        }
        final Minecraft client = mc;
        Collections.sort(active, new Comparator<Module>() {
            @Override
            public int compare(Module first, Module second) {
                return client.fontRenderer.getStringWidth(second.getName())
                        - client.fontRenderer.getStringWidth(first.getName());
            }
        });
        int y = 4;
        int index = 0;
        for (Module module : active) {
            String name = module.getName();
            int textWidth = mc.fontRenderer.getStringWidth(name);
            int x = screenWidth - textWidth - 8;
            int color = gradientColor(index, active.size());
            drawRect(x - 5, y, screenWidth, y + 12, 0xB0140B22);
            drawRect(screenWidth - 2, y, screenWidth, y + 12, color);
            mc.fontRenderer.drawStringWithShadow(name, x, y + 2, color);
            y += 13;
            index++;
        }
    }

    private void drawInfo(Minecraft mc, int screenHeight) {
        List<String> lines = new ArrayList<String>();
        FpsCounter fps = NXClient.moduleManager.get(FpsCounter.class);
        Coordinates coordinates = NXClient.moduleManager.get(Coordinates.class);
        if (fps != null && fps.isEnabled()) {
            lines.add("FPS " + Minecraft.getDebugFPS());
        }
        if (coordinates != null && coordinates.isEnabled()) {
            lines.add(String.format("XYZ %.0f %.0f %.0f", mc.player.posX, mc.player.posY, mc.player.posZ));
        }
        int y = screenHeight - 6 - lines.size() * 11;
        for (String line : lines) {
            int width = mc.fontRenderer.getStringWidth(line);
            drawRect(4, y - 2, 10 + width, y + 9, 0xB0140B22);
            drawRect(4, y - 2, 6, y + 9, Theme.ACCENT);
            mc.fontRenderer.drawStringWithShadow(line, 10, y, Theme.TEXT);
            y += 11;
        }
    }

    private int gradientColor(int index, int total) {
        if (total <= 1) {
            return Theme.ACCENT;
        }
        float ratio = index / (float) (total - 1);
        int r = (int) (157 + (0 - 157) * ratio);
        int g = (int) (78 + (229 - 78) * ratio);
        int b = (int) (221 + (160 - 221) * ratio);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
