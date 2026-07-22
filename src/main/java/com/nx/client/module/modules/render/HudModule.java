package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;

public class HudModule extends Module {

    public HudModule() {
        super("HUD", "Shows the on-screen NX Client overlay", Category.RENDER);
        setEnabledSilently(true);
    }
}
