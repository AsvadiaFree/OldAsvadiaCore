package fr.skyfighttv.acore.Utils;

import org.bukkit.Material;

public enum Plantations {
    WHEAT(Material.WHEAT),
    Carrot(Material.CARROT),
    Potato(Material.POTATO),
    BEETROOT_SEEDS(Material.BEETROOT_SEEDS),
    SWEET_BERRY_BUSH(Material.SWEET_BERRY_BUSH),
    PUMPKIN_SEEDS(Material.PUMPKIN_SEEDS),
    MELON_SEEDS(Material.MELON_SEEDS),
    NETHER_WART(Material.NETHER_WART);

    private final Material material;

    public Material getMaterial() {
        return material;
    }

    Plantations(Material material) {
        this.material = material;
    }
}
