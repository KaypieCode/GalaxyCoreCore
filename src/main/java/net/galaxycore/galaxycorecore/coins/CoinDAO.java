package net.galaxycore.galaxycorecore.coins;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.security.InvalidParameterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Getter
public class CoinDAO {
    private PlayerLoader player;
    private final Plugin plugin;

    public CoinDAO(PlayerLoader player, Plugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    @SneakyThrows
    public void transact(PlayerLoader transactionPartner, long playerAccountChange, @NonNull String reason) {

        if (get() - playerAccountChange < 0)
            throw new PlayerTransactionError();

        if (new CoinDAO(transactionPartner, plugin).get() + playerAccountChange < 0)
            throw new PartnerTransactionError();


        GalaxyCoreCore core = CoreProvider.getCore();
        PreparedStatement stmt = core.getDatabaseConfiguration().getConnection().prepareStatement("INSERT INTO `core_coins_transactions` (player, transaction_partner, amount, reason) VALUES (?, ?, ?, ?)");

        StringBuilder finalReason = new StringBuilder();
        finalReason.append("UID.");
        finalReason.append(System.currentTimeMillis());
        finalReason.append(";P.");
        finalReason.append(player.getId());
        finalReason.append(".");

        if (transactionPartner == null) {
            finalReason.append("C<P");
            finalReason.append(plugin.getName());
            finalReason.append(":");
            finalReason.append(plugin.getDescription().getVersion());
            finalReason.append(">");
        } else
            finalReason.append(transactionPartner.getId());

        finalReason.append("R.");
        finalReason.append(reason);

        if (finalReason.toString().length() > 255)
            throw new InvalidParameterException("Transaction Reason too long");

        stmt.setInt(1, player.getId());
        stmt.setInt(2, transactionPartner == null ? -1 : transactionPartner.getId());
        stmt.setInt(3, Math.toIntExact(playerAccountChange));
        stmt.setString(4, finalReason.toString());
        stmt.executeUpdate();
        stmt.close();

        PreparedStatement p1update = core.getDatabaseConfiguration().getConnection().prepareStatement("UPDATE `core_playercache` SET `coins`=`coins`-? WHERE `id`=?");
        p1update.setInt(1, Math.toIntExact(playerAccountChange));
        p1update.setInt(2, player.getId());
        p1update.executeUpdate();
        p1update.close();

        if (transactionPartner != null) {
            PreparedStatement p2update = core.getDatabaseConfiguration().getConnection().prepareStatement("UPDATE `core_playercache` SET `coins`=`coins`+? WHERE `id`=?");
            p2update.setInt(1, Math.toIntExact(playerAccountChange));
            p2update.setInt(2, transactionPartner.getId());

            p2update.executeUpdate();
            p2update.close();
        }

        player = PlayerLoader.loadNew(Bukkit.getPlayer(player.getUuid()));

        if (transactionPartner != null) {
            try {
                PlayerLoader.loadNew(Bukkit.getPlayer(transactionPartner.getUuid()));
            } catch (Exception ignored) {
            }
        }
    }

    @SneakyThrows
    public long get() {
        PreparedStatement preparedStatement = CoreProvider.getCore().getDatabaseConfiguration().getConnection().prepareStatement("SELECT coins FROM core_playercache WHERE id = ?");

        preparedStatement.setInt(1, player.getId());

        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next())
            throw new RuntimeException("Failed to get Coins");

        long coins = rs.getInt(1);
        rs.close();

        return coins;
    }
}
