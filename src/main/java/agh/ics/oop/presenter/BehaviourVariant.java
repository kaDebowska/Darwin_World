package agh.ics.oop.presenter;
public enum BehaviourVariant {
    NORMAL_ANIMAL("Full predestination"),
    CRAZY_ANIMAL("A little bit of madness");

    private final String displayName;

    BehaviourVariant(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() {
        return displayName;
    }
}