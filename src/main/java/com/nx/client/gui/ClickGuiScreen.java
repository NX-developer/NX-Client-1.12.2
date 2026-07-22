package com.nx.client.gui;

import com.nx.client.NXClient;
import com.nx.client.addon.LoadedAddon;
import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.BooleanSetting;
import com.nx.client.settings.NumberSetting;
import com.nx.client.settings.Setting;
import com.nx.client.util.Lang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ClickGuiScreen extends GuiScreen {

    private static final int WINDOW_WIDTH = 440;
    private static final int WINDOW_HEIGHT = 260;
    private static final int HEADER_HEIGHT = 28;
    private static final int SIDEBAR_WIDTH = 112;
    private static final int SEARCH_HEIGHT = 18;
    private static final int FOOTER_HEIGHT = 18;
    private static final int ROW_HEIGHT = 20;
    private static final int SETTING_HEIGHT = 15;

    private final Set<Module> expanded = new HashSet<Module>();

    private Category selected = Category.COMBAT;
    private boolean addonsTab;
    private String search = "";
    private NumberSetting draggingSlider;
    private Module bindingModule;
    private String footerText = "";

    private int windowX;
    private int windowY;

    @Override
    public void initGui() {
        windowX = (this.width - WINDOW_WIDTH) / 2;
        windowY = (this.height - WINDOW_HEIGHT) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        footerText = "";

        drawRect(windowX, windowY, windowX + WINDOW_WIDTH, windowY + WINDOW_HEIGHT, Theme.WINDOW);
        drawRect(windowX, windowY, windowX + WINDOW_WIDTH, windowY + HEADER_HEIGHT, Theme.HEADER);
        this.fontRenderer.drawStringWithShadow("NX", windowX + 12, windowY + 10, Theme.TEXT);
        int nxWidth = this.fontRenderer.getStringWidth("NX");
        this.fontRenderer.drawStringWithShadow("CLIENT", windowX + 14 + nxWidth, windowY + 10, Theme.TEXT_DIM);
        String version = "v" + NXClient.VERSION;
        this.fontRenderer.drawStringWithShadow(version,
                windowX + WINDOW_WIDTH - this.fontRenderer.getStringWidth(version) - 12, windowY + 10, Theme.TEXT);

        drawSidebar(mouseX, mouseY);
        if (addonsTab) {
            drawAddons();
        } else {
            drawSearch();
            drawModules(mouseX, mouseY);
        }
        drawFooter();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawSidebar(int mouseX, int mouseY) {
        int x = windowX;
        int y = windowY + HEADER_HEIGHT;
        int height = WINDOW_HEIGHT - HEADER_HEIGHT - FOOTER_HEIGHT;
        drawRect(x, y, x + SIDEBAR_WIDTH, y + height, Theme.SIDEBAR);
        int itemY = y + 8;
        for (Category category : Category.values()) {
            boolean active = !addonsTab && category == selected;
            boolean hovered = isInside(mouseX, mouseY, x + 6, itemY, SIDEBAR_WIDTH - 12, 22);
            drawRect(x + 6, itemY, x + SIDEBAR_WIDTH - 6, itemY + 22,
                    active ? Theme.ROW_ACTIVE : hovered ? Theme.ROW_HOVER : Theme.SIDEBAR);
            if (active) {
                drawRect(x + 6, itemY, x + 8, itemY + 22, Theme.ACCENT);
            }
            this.fontRenderer.drawString(category.getLabel(), x + 16, itemY + 7, active ? Theme.TEXT : Theme.TEXT_DIM);
            String count = String.valueOf(NXClient.moduleManager.getByCategory(category).size());
            this.fontRenderer.drawString(count,
                    x + SIDEBAR_WIDTH - 16 - this.fontRenderer.getStringWidth(count), itemY + 7, Theme.TEXT_FAINT);
            itemY += 26;
        }
        boolean addonsHovered = isInside(mouseX, mouseY, x + 6, itemY, SIDEBAR_WIDTH - 12, 22);
        drawRect(x + 6, itemY, x + SIDEBAR_WIDTH - 6, itemY + 22,
                addonsTab ? Theme.ROW_ACTIVE : addonsHovered ? Theme.ROW_HOVER : Theme.SIDEBAR);
        if (addonsTab) {
            drawRect(x + 6, itemY, x + 8, itemY + 22, Theme.ACCENT);
        }
        this.fontRenderer.drawString(Lang.gui("addons", "Addons"), x + 16, itemY + 7,
                addonsTab ? Theme.TEXT : Theme.TEXT_DIM);
        String addonCount = String.valueOf(NXClient.addonManager.getAddons().size());
        this.fontRenderer.drawString(addonCount,
                x + SIDEBAR_WIDTH - 16 - this.fontRenderer.getStringWidth(addonCount), itemY + 7, Theme.TEXT_FAINT);

        int enabled = 0;
        for (Module module : NXClient.moduleManager.getModules()) {
            if (module.isEnabled()) {
                enabled++;
            }
        }
        this.fontRenderer.drawString(enabled + " " + Lang.gui("active", "active"),
                x + 16, y + height - 14, Theme.TEXT_FAINT);
    }

    private void drawSearch() {
        int x = windowX + SIDEBAR_WIDTH;
        int y = windowY + HEADER_HEIGHT;
        int width = WINDOW_WIDTH - SIDEBAR_WIDTH;
        drawRect(x, y, x + width, y + SEARCH_HEIGHT, Theme.SEARCH_BG);
        String display = search.isEmpty() ? Lang.gui("search", "Search modules...") : search;
        this.fontRenderer.drawString(display, x + 10, y + 5, search.isEmpty() ? Theme.TEXT_FAINT : Theme.TEXT);
    }

    private void drawModules(int mouseX, int mouseY) {
        int x = windowX + SIDEBAR_WIDTH;
        int y = windowY + HEADER_HEIGHT + SEARCH_HEIGHT;
        int width = WINDOW_WIDTH - SIDEBAR_WIDTH;
        int height = WINDOW_HEIGHT - HEADER_HEIGHT - SEARCH_HEIGHT - FOOTER_HEIGHT;
        drawRect(x, y, x + width, y + height, Theme.CONTENT);

        int rowY = y + 4;
        for (Module module : getVisibleModules()) {
            if (rowY > y + height - 10) {
                break;
            }
            drawRow(module, x, rowY, width, mouseX, mouseY);
            rowY += ROW_HEIGHT;
            if (expanded.contains(module)) {
                drawBindRow(module, x, rowY, width, mouseX, mouseY);
                rowY += SETTING_HEIGHT;
                for (Setting<?> setting : module.getSettings()) {
                    drawSetting(setting, x, rowY, width, mouseX);
                    rowY += SETTING_HEIGHT;
                }
            }
            rowY += 2;
        }
    }

    private void drawRow(Module module, int x, int y, int width, int mouseX, int mouseY) {
        boolean hovered = isInside(mouseX, mouseY, x + 6, y, width - 12, ROW_HEIGHT);
        drawRect(x + 6, y, x + width - 6, y + ROW_HEIGHT,
                module.isEnabled() ? Theme.ROW_ACTIVE : hovered ? Theme.ROW_HOVER : Theme.ROW);
        if (hovered) {
            footerText = module.getDescription();
        }
        this.fontRenderer.drawString(module.getName(), x + 16, y + 6,
                module.isEnabled() ? Theme.TEXT : Theme.TEXT_DIM);

        String key = keyLabel(module);
        if (!key.isEmpty()) {
            this.fontRenderer.drawString(key,
                    x + width - 52 - this.fontRenderer.getStringWidth(key), y + 6, Theme.TEXT_FAINT);
        }
        int pillX = x + width - 44;
        drawRect(pillX, y + 5, pillX + 22, y + 15, module.isEnabled() ? Theme.ENABLED : Theme.DISABLED);
        int knobX = module.isEnabled() ? pillX + 13 : pillX + 1;
        drawRect(knobX, y + 6, knobX + 8, y + 14, Theme.WINDOW);
        this.fontRenderer.drawString(expanded.contains(module) ? "-" : "+", x + width - 16, y + 6, Theme.TEXT_DIM);
    }

    private void drawBindRow(Module module, int x, int y, int width, int mouseX, int mouseY) {
        boolean hovered = isInside(mouseX, mouseY, x + 16, y, width - 28, SETTING_HEIGHT);
        drawRect(x + 16, y, x + width - 12, y + SETTING_HEIGHT, hovered ? Theme.ROW_HOVER : Theme.CONTENT);
        this.fontRenderer.drawString(Lang.gui("keybind", "Keybind"), x + 24, y + 4, Theme.TEXT_DIM);
        String label;
        int color;
        if (bindingModule == module) {
            label = Lang.gui("presskey", "Press a key...");
            color = Theme.ENABLED;
        } else {
            String key = keyLabel(module);
            label = key.isEmpty() ? Lang.gui("none", "None") : key;
            color = key.isEmpty() ? Theme.TEXT_FAINT : Theme.ACCENT;
        }
        this.fontRenderer.drawString(label,
                x + width - 24 - this.fontRenderer.getStringWidth(label), y + 4, color);
        if (hovered) {
            footerText = Lang.gui("bindhint", "Click to bind, then press a key. Escape clears the bind.");
        }
    }

    private void drawSetting(Setting<?> setting, int x, int y, int width, int mouseX) {
        drawRect(x + 16, y, x + width - 12, y + SETTING_HEIGHT, Theme.CONTENT);
        if (setting instanceof BooleanSetting) {
            BooleanSetting bool = (BooleanSetting) setting;
            this.fontRenderer.drawString(setting.getDisplayName(), x + 24, y + 4, Theme.TEXT_DIM);
            int boxX = x + width - 34;
            drawRect(boxX, y + 3, boxX + 10, y + 13, bool.isOn() ? Theme.ENABLED : Theme.DISABLED);
        } else if (setting instanceof NumberSetting) {
            NumberSetting number = (NumberSetting) setting;
            this.fontRenderer.drawString(setting.getDisplayName(), x + 24, y + 2, Theme.TEXT_DIM);
            String value = number.format();
            this.fontRenderer.drawString(value,
                    x + width - 24 - this.fontRenderer.getStringWidth(value), y + 2, Theme.ACCENT);
            int trackX = x + 24;
            int trackWidth = width - 52;
            drawRect(trackX, y + 11, trackX + trackWidth, y + 12, Theme.SLIDER_TRACK);
            double ratio = (number.getValue() - number.getMin()) / (number.getMax() - number.getMin());
            int filled = (int) (trackWidth * ratio);
            drawRect(trackX, y + 11, trackX + filled, y + 12, Theme.ACCENT);
            drawRect(trackX + filled - 2, y + 9, trackX + filled + 3, y + 14, Theme.TEXT);
            if (draggingSlider == number) {
                double clamped = Math.min(1.0, Math.max(0.0, (mouseX - trackX) / (double) trackWidth));
                number.setClamped(number.getMin() + clamped * (number.getMax() - number.getMin()));
            }
        }
    }

    private void drawAddons() {
        int x = windowX + SIDEBAR_WIDTH;
        int y = windowY + HEADER_HEIGHT;
        int width = WINDOW_WIDTH - SIDEBAR_WIDTH;
        int height = WINDOW_HEIGHT - HEADER_HEIGHT - FOOTER_HEIGHT;
        drawRect(x, y, x + width, y + height, Theme.CONTENT);
        List<LoadedAddon> addons = NXClient.addonManager.getAddons();
        if (addons.isEmpty()) {
            this.fontRenderer.drawString(Lang.gui("noaddons", "No addons installed"), x + 16, y + 16, Theme.TEXT_DIM);
            this.fontRenderer.drawString(Lang.gui("addonhint1", "Drop an NX Client addon jar into your"),
                    x + 16, y + 34, Theme.TEXT_FAINT);
            this.fontRenderer.drawString(Lang.gui("addonhint2", "mods folder and it loads automatically."),
                    x + 16, y + 46, Theme.TEXT_FAINT);
            this.fontRenderer.drawString(Lang.gui("addonhint3", "See docs/ADDONS.md to build your own."),
                    x + 16, y + 64, Theme.TEXT_FAINT);
            return;
        }
        int rowY = y + 8;
        for (LoadedAddon addon : addons) {
            drawRect(x + 8, rowY, x + width - 8, rowY + 28, Theme.ROW);
            this.fontRenderer.drawString(addon.getName(), x + 16, rowY + 5,
                    addon.isFailed() ? 0xFFFF5555 : Theme.TEXT);
            String version = "v" + addon.getVersion();
            this.fontRenderer.drawString(version,
                    x + width - 20 - this.fontRenderer.getStringWidth(version), rowY + 5, Theme.TEXT_FAINT);
            String info = addon.isFailed()
                    ? Lang.gui("addonfailed", "Failed to load")
                    : addon.getAuthor() + "  -  " + addon.getModuleCount() + " " + Lang.gui("modules", "modules");
            this.fontRenderer.drawString(info, x + 16, rowY + 17, Theme.TEXT_FAINT);
            rowY += 32;
        }
    }

    private void drawFooter() {
        int x = windowX;
        int y = windowY + WINDOW_HEIGHT - FOOTER_HEIGHT;
        drawRect(x, y, x + WINDOW_WIDTH, y + FOOTER_HEIGHT, Theme.FOOTER);
        String text = footerText.isEmpty()
                ? Lang.gui("footer", "Left click to toggle  |  Right click for settings  |  Right Shift to close")
                : footerText;
        this.fontRenderer.drawString(text, x + 12, y + 5, Theme.TEXT_FAINT);
    }

    private String keyLabel(Module module) {
        int code = module.getKeyBinding().getKeyCode();
        if (code == Keyboard.KEY_NONE) {
            return "";
        }
        return Keyboard.getKeyName(code);
    }

    private List<Module> getVisibleModules() {
        List<Module> result = new ArrayList<Module>();
        String query = search.toLowerCase();
        if (!query.isEmpty()) {
            for (Module module : NXClient.moduleManager.getModules()) {
                if (module.getName().toLowerCase().contains(query)) {
                    result.add(module);
                }
            }
            return result;
        }
        result.addAll(NXClient.moduleManager.getByCategory(selected));
        return result;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int sidebarX = windowX;
        int sidebarY = windowY + HEADER_HEIGHT + 8;
        for (Category category : Category.values()) {
            if (isInside(mouseX, mouseY, sidebarX + 6, sidebarY, SIDEBAR_WIDTH - 12, 22)) {
                selected = category;
                addonsTab = false;
                return;
            }
            sidebarY += 26;
        }
        if (isInside(mouseX, mouseY, sidebarX + 6, sidebarY, SIDEBAR_WIDTH - 12, 22)) {
            addonsTab = true;
            return;
        }
        if (addonsTab) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }

        int contentX = windowX + SIDEBAR_WIDTH;
        int contentWidth = WINDOW_WIDTH - SIDEBAR_WIDTH;
        int rowY = windowY + HEADER_HEIGHT + SEARCH_HEIGHT + 4;
        for (Module module : getVisibleModules()) {
            if (isInside(mouseX, mouseY, contentX + 6, rowY, contentWidth - 12, ROW_HEIGHT)) {
                if (mouseButton == 0) {
                    module.toggle();
                } else if (mouseButton == 1) {
                    if (!expanded.remove(module)) {
                        expanded.add(module);
                    } else if (bindingModule == module) {
                        bindingModule = null;
                    }
                }
                return;
            }
            rowY += ROW_HEIGHT;
            if (expanded.contains(module)) {
                if (isInside(mouseX, mouseY, contentX + 16, rowY, contentWidth - 28, SETTING_HEIGHT)) {
                    if (mouseButton == 0) {
                        bindingModule = bindingModule == module ? null : module;
                    }
                    return;
                }
                rowY += SETTING_HEIGHT;
                for (Setting<?> setting : module.getSettings()) {
                    if (isInside(mouseX, mouseY, contentX + 16, rowY, contentWidth - 28, SETTING_HEIGHT)) {
                        if (setting instanceof BooleanSetting && mouseButton == 0) {
                            ((BooleanSetting) setting).toggle();
                            return;
                        }
                        if (setting instanceof NumberSetting && mouseButton == 0) {
                            draggingSlider = (NumberSetting) setting;
                            return;
                        }
                    }
                    rowY += SETTING_HEIGHT;
                }
            }
            rowY += 2;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingSlider = null;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (bindingModule != null) {
            KeyBinding binding = bindingModule.getKeyBinding();
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) {
                binding.setKeyCode(Keyboard.KEY_NONE);
            } else {
                binding.setKeyCode(keyCode);
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            this.mc.gameSettings.saveOptions();
            bindingModule = null;
            return;
        }
        if (keyCode == Keyboard.KEY_BACK && !search.isEmpty()) {
            search = search.substring(0, search.length() - 1);
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
            return;
        }
        if (typedChar >= ' ') {
            search += typedChar;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    private boolean isInside(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
