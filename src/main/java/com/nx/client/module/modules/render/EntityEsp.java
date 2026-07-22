package com.nx.client.module.modules.render;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.BooleanSetting;

public class EntityEsp extends Module {

    private final BooleanSetting hostile = new BooleanSetting("Hostile", true);
    private final BooleanSetting passive = new BooleanSetting("Passive", true);
    private final BooleanSetting players = new BooleanSetting("Players", true);

    public EntityEsp() {
        super("EntityESP", "Draws boxes around living entities", Category.RENDER);
        addSetting(hostile);
        addSetting(passive);
        addSetting(players);
    }

    public boolean showHostile() {
        return hostile.isOn();
    }

    public boolean showPassive() {
        return passive.isOn();
    }

    public boolean showPlayers() {
        return players.isOn();
    }
}
