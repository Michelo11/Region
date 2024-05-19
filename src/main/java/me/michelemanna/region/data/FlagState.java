package me.michelemanna.region.data;

public enum FlagState {
    EVERYONE,
    WHITELIST,
    NONE;

    public FlagState next() {
        return values()[(ordinal() + 1) % values().length];
    }
}