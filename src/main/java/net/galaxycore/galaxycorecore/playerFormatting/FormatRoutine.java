package net.galaxycore.galaxycorecore.playerFormatting;

import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import org.bukkit.Server;
import org.slf4j.Logger;

public class FormatRoutine {
    private final Thread routine;
    private final Logger logger;
    private final Server server;
    private final TablistFormatter tablistFormatter;

    public FormatRoutine(Logger logger, Server server, TablistFormatter tablistFormatter) {
        this.logger = logger;
        this.server = server;
        this.tablistFormatter = tablistFormatter;
        routine = new SimpleFormatRoutine();
        logger.info("Starting Format Routine...");
        routine.start();
    }

    public void shutdown() {
        routine.interrupt();
        logger.info("Shutting Down Format Routine...");
        try {
            routine.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class SimpleFormatRoutine extends Thread {
        @Override
        public void run() {
            boolean active = true;

            while (active) {
                server.getOnlinePlayers().forEach(player -> {
                    try {
                        String expectedName = tablistFormatter.calculatePlayerListName(new LuckPermsApiWrapper(player));
                        // Paper wants its Components, but it wouldn't be performant to use those here
                        //noinspection deprecation
                        String actualName = player.getPlayerListName();

                        if (!expectedName.equals(actualName))
                            // Paper wants its Components, but it wouldn't be performant to use those here, FY Paper
                            //noinspection deprecation
                            player.setPlayerListName(expectedName);
                    } catch (Exception ignored) {
                        logger.warn("Failed to calculate or update the Player " + player.getName());
                    }
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    active = false;
                    logger.info("Unloading...");
                }
            }
        }
    }
}
