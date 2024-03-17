package dev.philtraeger;

import com.mojang.authlib.GameProfile;
import dev.philtraeger.db.DBUser;
import dev.philtraeger.db.Database;
import net.minecraft.server.Whitelist;
import net.minecraft.server.WhitelistEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MaxWhitelist {
    private static MaxWhitelist instance;
    private static Whitelist whitelist;

    private static final Logger logger = LogManager.getLogger(MaxWhitelist.class);

    private MaxWhitelist() {
        String currentDir = System.getProperty("user.dir");
        whitelist = new Whitelist(new File(currentDir + "/whitelist.json"));
        try {
            whitelist.load();
            List<DBUser> list = Database.getInstance().getAllUser();
            for(DBUser user : list) {
                whitelist.add(
                        new WhitelistEntry(new GameProfile(UUID.fromString(user.uuidMinecraft), user.usernameMinecraft))
                );
            }
            whitelist.save();
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
        logger.info("Whitelist Loaded");
    }

    public static MaxWhitelist getInstance() {
        if (instance == null){
            instance = new MaxWhitelist();
        }
        return instance;
    }

    public Whitelist getWhitelist() {
        return whitelist;
    }

    public void remove(String uuid, String username) {
        try {
            instance.getWhitelist().load();
            instance.getWhitelist().remove(new WhitelistEntry(new GameProfile(UUID.fromString(uuid), username)));
            instance.getWhitelist().save();
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
    }

    public void add(String uuid, String username) {
        try {
            instance.getWhitelist().load();
            instance.getWhitelist().add(new WhitelistEntry(new GameProfile(UUID.fromString(uuid), username)));
            instance.getWhitelist().save();
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
    }
}
