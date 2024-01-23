package agh.ics.oop.presenter;

public enum MapVariant {
    GLOBE_MAP("Forested equator"),
    CARCASS_MAP("Life-giving carcass");

    private final String displayName;

    MapVariant(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() {
        return displayName;
    }
}
