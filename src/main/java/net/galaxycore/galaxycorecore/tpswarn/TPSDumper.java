package net.galaxycore.galaxycorecore.tpswarn;

import co.aikar.timings.Timings;
import co.aikar.timings.TimingsReportListener;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.configuration.PrefixProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TPSDumper {
    public static String dumpTimingData(double tps) {
        if (Timings.isTimingsEnabled()) {
            return "Timing Data unavailable";
        }

        Timings.reset();
        Timings.setTimingsEnabled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                Timings.setTimingsEnabled(false);
                TimingsReportListener reportListener = new TimingsReportListener(Bukkit.getConsoleSender());
                Timings.generateReport(reportListener);
                String url = reportListener.getTimingsURL();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("galaxycore.tpswarn.view")) {
                        player.sendMessage(PrefixProvider.getPrefix() + "§7Got a TPS Dump (" + url + "/at " + tps + ")");
                    }
                }
            }
        }.runTaskLaterAsynchronously(CoreProvider.getCore(), 60 * 20L);

        return "Timing Data will be available... Wait a minute";
    }
}
