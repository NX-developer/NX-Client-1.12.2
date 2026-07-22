package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;
import java.lang.reflect.Field;

public class Timer extends Module {

    private static final float DEFAULT_TICK_LENGTH = 50.0F;

    private final NumberSetting multiplier = new NumberSetting("Speed", 1.5, 0.5, 5.0, 0.1);

    private Object timerObject;
    private Field tickLengthField;
    private boolean resolved;

    public Timer() {
        super("Timer", "Changes how fast the game ticks around you", Category.MOVEMENT);
        addSetting(multiplier);
    }

    @Override
    public void onEnable() {
        resolve();
    }

    @Override
    public void onDisable() {
        apply(DEFAULT_TICK_LENGTH);
    }

    @Override
    public void onTick() {
        apply((float) (DEFAULT_TICK_LENGTH / multiplier.getValue()));
    }

    private void resolve() {
        if (resolved) {
            return;
        }
        resolved = true;
        try {
            for (Field field : mc.getClass().getDeclaredFields()) {
                if (!field.getType().getName().endsWith("Timer")) {
                    continue;
                }
                field.setAccessible(true);
                Object timer = field.get(mc);
                if (timer == null) {
                    continue;
                }
                for (Field inner : timer.getClass().getDeclaredFields()) {
                    if (inner.getType() != float.class) {
                        continue;
                    }
                    inner.setAccessible(true);
                    if (Math.abs(inner.getFloat(timer) - DEFAULT_TICK_LENGTH) < 0.001F) {
                        timerObject = timer;
                        tickLengthField = inner;
                        return;
                    }
                }
            }
        } catch (Exception ignored) {
            timerObject = null;
            tickLengthField = null;
        }
    }

    private void apply(float value) {
        if (timerObject == null || tickLengthField == null) {
            return;
        }
        try {
            tickLengthField.setFloat(timerObject, value);
        } catch (Exception ignored) {
        }
    }
}
