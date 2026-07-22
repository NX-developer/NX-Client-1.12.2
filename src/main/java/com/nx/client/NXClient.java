package com.nx.client;

import com.nx.client.addon.AddonManager;
import com.nx.client.config.ConfigManager;
import com.nx.client.gui.ClickGuiScreen;
import com.nx.client.hud.HudOverlay;
import com.nx.client.hud.WorldEspRenderer;
import com.nx.client.module.ModuleManager;
import com.nx.client.module.Modules;
import com.nx.client.module.modules.combat.Criticals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = NXClient.MOD_ID, name = NXClient.MOD_NAME, version = NXClient.VERSION, clientSideOnly = true)
public class NXClient {

    public static final String MOD_ID = "nxclient";
    public static final String MOD_NAME = "NX Client";
    public static final String VERSION = "1.1.0";

    public static ModuleManager moduleManager;
    public static AddonManager addonManager;
    public static KeyBinding openGuiKey;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        moduleManager = new ModuleManager();
        Modules.registerAll(moduleManager);
        addonManager = new AddonManager();
        addonManager.loadAll(event.getAsmData());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        openGuiKey = new KeyBinding("key.nxclient.opengui", Keyboard.KEY_RSHIFT, "category.nxclient");
        ClientRegistry.registerKeyBinding(openGuiKey);
        ConfigManager.load();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new HudOverlay());
        MinecraftForge.EVENT_BUS.register(new WorldEspRenderer());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        while (openGuiKey.isPressed()) {
            mc.displayGuiScreen(new ClickGuiScreen());
        }
        moduleManager.onTick(mc);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getEntityPlayer() != mc.player) {
            return;
        }
        Criticals criticals = moduleManager.get(Criticals.class);
        if (criticals != null && criticals.shouldApply()) {
            criticals.fakeJump();
        }
    }
}
