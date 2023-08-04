package net.asodev.islandutils.state.faction;

public enum Faction {
    AQUA("Aqua Axolotls"),
    BLUE("Blue Bats"),
    CYAN("Cyan Coyotes"),
    GREEN("Green Geckos"),
    LIME("Lime Llamas"),
    ORANGE("Orange Ocelots"),
    PINK("Pink Parrots"),
    PURPLE("Purple Pandas"),
    RED("Red Rabbits"),
    YELLOW("Yellow Yaks");

    private final String title;
    Faction(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
