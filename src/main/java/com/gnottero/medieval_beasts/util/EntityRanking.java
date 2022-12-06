package com.gnottero.medieval_beasts.util;

public class EntityRanking {

    public enum Ranking {
        F("F", 1, 1, 1, 1, 15, 8, 70),
        E("E", 2, 0, 0, 0, 30, 10, 30),
        D("D", 3, 255, 255, 255, 30, 10, 30),
        C("C", 4, 91, 202, 126, 30, 8, 70),
        B("B", 5, 88, 190, 203, 30, 8, 70),
        A("A", 6, 129, 95, 204, 30, 8, 70),
        S("S", 7, 199, 86, 81, 30, 8, 70),
        SS("SS", 8, 255, 215, 0, 30, 8, 70);

        private String name;
        private int attrMultiplier;
        private int red;
        private int green;
        private int blue;
        private int range;
        private int duration;
        private int cooldown;

        static
        public final Ranking[] values = values();

        public Ranking prev() {
            return values[(ordinal() - 1  + values.length) % values.length];
        }

        public Ranking next() {
            return values[(ordinal() + 1) % values.length];
        }

        Ranking(String name, int attrMultiplier, int red, int green, int blue, int range, int duration, int cooldown) {
            this.name = name;
            this.attrMultiplier = attrMultiplier;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.range = range;
            this.duration = duration;
            this.cooldown = cooldown;
        }

        public String getName() {
            return this.name;
        }

        public int getAttrMultiplier() {
            return attrMultiplier;
        }

        public float getRed() {
            return (float) red/255;
        }

        public float getGreen() {
            return (float) green/255;
        }

        public float getBlue() {
            return (float) blue/255;
        }

        public int getRange() {
            return range;
        }

        public int getDuration() {
            return duration * 20;
        }

        public int getCooldown() {
            return cooldown * 20;
        }
    }

}
