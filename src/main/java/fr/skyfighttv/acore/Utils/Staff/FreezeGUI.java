package fr.skyfighttv.acore.Utils.Staff;

import fr.ChadOW.cinventory.CInventory;
import fr.ChadOW.cinventory.CItem;
import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FreezeGUI {
    private static CInventory freezeInventory;
    private CItem revealItem;
    private CItem infoItem;
    private CItem acceptReveal;
    private CItem denyReveal;
    private CItem accepted;

    public FreezeGUI(Player player, Player Sender) {
        YamlConfiguration config = FileManager.getValues().get(Files.Staff);

        freezeInventory = new CInventory(27, config.getString("Gui.FreezeGUI.Title"));
        freezeInventory.setClosable(false);

        revealItem = new CItem(new ItemCreator(Material.getMaterial(config.getString("Gui.FreezeGUI.Items.Reavel.Material")), 0)
                .setName(config.getString("Gui.FreezeGUI.Items.Reavel.Name"))
                .setLores(config.getStringList("Gui.FreezeGUI.Items.Reavel.Lore")))
                .setSlot(config.getInt("Gui.FreezeGUI.Items.Reavel.Slot"));
        infoItem = new CItem(new ItemCreator(Material.getMaterial(config.getString("Gui.FreezeGUI.Items.Info.Material")),0)
                .setName(config.getString("Gui.FreezeGUI.Items.Info.Name"))
                .setLores(config.getStringList("Gui.FreezeGUI.Items.Info.Lore")))
                .setSlot(config.getInt("Gui.FreezeGUI.Items.Info.Slot"));

        revealItem.addEvent((cInventory, cItem, player1, clickContext) -> {
            freezeInventory.removeElement(revealItem);
            freezeInventory.removeElement(infoItem);

            acceptReveal = new CItem(new ItemCreator(Material.getMaterial(config.getString("Gui.FreezeGUI.Items.AcceptReavel.Material")),0)
                    .setName(config.getString("Gui.FreezeGUI.Items.AcceptReavel.Name"))
                    .setLores(config.getStringList("Gui.FreezeGUI.Items.AcceptReavel.Lore")))
                    .setSlot(config.getInt("Gui.FreezeGUI.Items.AcceptReavel.Slot"));
            denyReveal = new CItem(new ItemCreator(Material.getMaterial(config.getString("Gui.FreezeGUI.Items.DenyReavel.Material")),0)
                    .setName(config.getString("Gui.FreezeGUI.Items.DenyReavel.Name"))
                    .setLores(config.getStringList("Gui.FreezeGUI.Items.DenyReavel.Lore")))
                    .setSlot(config.getInt("Gui.FreezeGUI.Items.DenyReavel.Slot"));

            acceptReveal.addEvent((cInventory1, cItem1, player2, clickContext1) -> {
                freezeInventory.removeElement(acceptReveal);
                freezeInventory.removeElement(denyReveal);

                accepted = new CItem(new ItemCreator(Material.getMaterial(config.getString("Gui.FreezeGUI.Items.IsReavel.Material")),0)
                        .setName(config.getString("Gui.FreezeGUI.Items.IsReavel.Name"))
                        .setLores(config.getStringList("Gui.FreezeGUI.Items.IsReavel.Lore")))
                        .setSlot(config.getInt("Gui.FreezeGUI.Items.IsReavel.Slot"));

                Sender.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Staff.AvouePlayer"));

                freezeInventory.addElement(accepted);
                freezeInventory.addElement(infoItem);
            });

            denyReveal.addEvent((cInventory1, cItem1, player2, clickContext1) -> {
                new FreezeGUI(player2, Sender);
            });

            freezeInventory.addElement(acceptReveal);
            freezeInventory.addElement(denyReveal);
        });

        freezeInventory.addElement(infoItem);
        freezeInventory.addElement(revealItem);

        freezeInventory.open(player);
    }

    public static void close(Player player) {
        freezeInventory.close(player);
    }
}
