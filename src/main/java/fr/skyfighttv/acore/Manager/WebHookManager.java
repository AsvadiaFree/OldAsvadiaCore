package fr.skyfighttv.acore.Manager;

import club.minnced.discord.webhook.WebhookClient;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;

public class WebHookManager {
    public static WebhookClient logTopLuck;
    public static WebhookClient logFaction;

    public WebHookManager() {
        logTopLuck = WebhookClient.withUrl(FileManager.getValues().get(Files.WebHooks).getString("logTopLuck.URL"));
        logFaction = WebhookClient.withUrl(FileManager.getValues().get(Files.WebHooks).getString("logFaction.URL"));

        logTopLuck.send("Started");
        logFaction.send("Started");
    }
}
