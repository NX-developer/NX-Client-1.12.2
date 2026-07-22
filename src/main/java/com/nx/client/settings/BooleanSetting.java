package com.nx.client.settings;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    public boolean isOn() {
        return getValue();
    }

    public void toggle() {
        setValue(!getValue());
    }
}
