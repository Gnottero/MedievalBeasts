package com.gnottero.medieval_beasts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MedievalBeastsConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Integer> REQUIRED_STEPS;
    public static final ForgeConfigSpec.ConfigValue<Double> ATTRIBUTE_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Float> RITUAL_TIME;

    static {
        BUILDER.comment("Config for Medieval Beasts");

        REQUIRED_STEPS = BUILDER.comment("> Set this value to the number of steps needed to get a Complete Egg").define("Required Steps", 1000);
        ATTRIBUTE_MULTIPLIER = BUILDER.comment("> Set this value to a decimal number indicating the multiplier for all the attributes, based on the rank").define("Attribute Multiplier", 1.5d);
        RITUAL_TIME = BUILDER.comment("> Set this value to the number of seconds needed for the upgrade ritual to reach completion").define("Ritual Time", 2.0f);

        SPEC = BUILDER.build();
    }

}
