package com.nx.client.settings;

import java.util.Locale;

public class NumberSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    public void setClamped(double value) {
        double snapped = Math.round(value / step) * step;
        setValue(Math.min(max, Math.max(min, snapped)));
    }

    public String format() {
        if (step >= 1.0) {
            return String.valueOf((int) Math.round(getValue()));
        }
        return String.format(Locale.ROOT, "%.2f", getValue());
    }
}
