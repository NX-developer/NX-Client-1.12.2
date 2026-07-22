package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.BooleanSetting;
import com.nx.client.settings.NumberSetting;

public class Nametags extends Module {

    private final NumberSetting scale = new NumberSetting("Scale", 1.4, 0.5, 4.0, 0.1);
    private final BooleanSetting health = new BooleanSetting("Health", true);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true);

    public Nametags() {
        super("Nametags", "Draws larger name tags with health above entities", Category.RENDER);
        addSetting(scale);
        addSetting(health);
        addSetting(players);
        addSetting(mobs);
    }

    public double getScale() {
        return scale.getValue();
    }

    public boolean showHealth() {
        return health.isOn();
    }

    public boolean showPlayers() {
        return players.isOn();
    }

    public boolean showMobs() {
        return mobs.isOn();
    }
}
