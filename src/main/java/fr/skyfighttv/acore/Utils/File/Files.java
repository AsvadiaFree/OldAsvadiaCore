package fr.skyfighttv.acore.Utils.File;

public enum Files {
    CustomDrops("CustomDrops"),
    Config("config"),
    Effects("Effects"),
    LuckyBlock("LuckyBlock"),
    Permissions("Permissions"),
    ItemStacks("ItemStacks"),
    ResourcePack("ResourcePack"),
    Staff("Staff"),
    Sanction("Sanction"),
    RTP("RTP"),
    HoeSell("HoeSell"),
    CustomCommands("CustomCommands"),
    WebHooks("WebHooks"),
    AutoLog("AutoLog"),
    DefaultPlayerFile("DefaultPlayerFile");

    private final String name;

    Files(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
