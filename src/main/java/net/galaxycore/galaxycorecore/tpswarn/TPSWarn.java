package net.galaxycore.galaxycorecore.tpswarn;

import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.utils.DiscordWebhook;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TPSWarn implements Runnable {

    private final GalaxyCoreCore galaxyCoreCore;

    private int count_id;
    private int check_id;

    public static int tickCount = 0;
    public static long[] ticks = new long[600];

    public TPSWarn(GalaxyCoreCore core) {
        this.galaxyCoreCore = core;
        init();
    }

    public static double getTPS() {
        return getTPS(20 * 10);
    }

    public static double getTPS(int ticks) {
        if (tickCount < ticks) {
            return 20.0D;
        }
        int target = (tickCount - 1 - ticks) % TPSWarn.ticks.length;
        long elapsed = System.currentTimeMillis() - TPSWarn.ticks[target];

        return ticks / (elapsed / 1000.0D);
    }

    @Override
    public void run() {
        ticks[(tickCount % ticks.length)] = System.currentTimeMillis();

        tickCount += 1;
    }

    public void init() {
        count_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(galaxyCoreCore, this, 100, 1);
        check_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(galaxyCoreCore, () -> {

            int minimalAllowedTPS = Integer.parseInt(galaxyCoreCore.getCoreNamespace().get("tpswarn.minimal_allowed_tps"));

            if (getTPS() < minimalAllowedTPS) {

                DiscordWebhook webhook = new DiscordWebhook(galaxyCoreCore.getCoreNamespace().get("tpswarn.webhook_url"));

                DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();

                embed.setTitle("**TPS DES SERVERS " + Bukkit.getServer().getName() + " NIEDRIG**");
                embed.setDescription("Die TPS des Servers sind aktuell " + getTPS() + " / 20");
                embed.setFooter(Bukkit.getServer().getName(), "");

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                embed.setAuthor(dateTimeFormatter.format(LocalDateTime.now()), "", "");

                webhook.addEmbed(embed);

                try {
                    webhook.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }, 0, 20 * 10);
    }

    public void shutdown() {
        Bukkit.getScheduler().cancelTask(count_id);
        Bukkit.getScheduler().cancelTask(check_id);
    }

}
