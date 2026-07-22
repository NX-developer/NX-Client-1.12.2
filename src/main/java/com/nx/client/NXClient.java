package com.nx.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = NXClient.MOD_ID, name = NXClient.MOD_NAME, version = NXClient.VERSION, clientSideOnly = true)
public class NXClient {

    public static final String MOD_ID = "nxclient";
    public static final String MOD_NAME = "NX Client";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }
}
