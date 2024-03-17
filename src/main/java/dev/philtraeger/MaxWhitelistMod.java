package dev.philtraeger;

import dev.philtraeger.commands.AutoComplete;
import dev.philtraeger.commands.DeleteUserCommand;
import dev.philtraeger.commands.ListUserCommand;
import dev.philtraeger.config.ModConfigs;
import dev.philtraeger.db.DBUser;
import dev.philtraeger.db.Database;
import dev.philtraeger.embeds.WhitelistEmbed;
import dev.philtraeger.events.MessageReceivedListener;
import dev.philtraeger.events.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MaxWhitelistMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("maxwhitelistmod");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        ModConfigs.registerConfigs();
        Database.getInstance();
        MaxWhitelist.getInstance();

		JDA jda = JDABuilder.createDefault(ModConfigs.BOT_TOKEN)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new MessageReceivedListener())
				.addEventListeners(new ReadyListener())
                .addEventListeners(new DeleteUserCommand())
                .addEventListeners(new AutoComplete())
                .addEventListeners(new ListUserCommand())
                .setActivity(Activity.playing("auf dem besten Minecraft Server EU-WEST Alla!"))
				.build();

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<DBUser> users = Database.getInstance().getAllUser();
                LOGGER.info("Refresh Whitelist");
                for(DBUser user : users){
                    if (user.wildcard) continue;
                    List<Role> roleList = Utils.getRolesFromUser(
                            Objects.requireNonNull(jda.getGuildById(ModConfigs.GUILD_ID)), user.discordID
                    );
                    if(!Utils.hasNoneRole(roleList)) return;
                    LOGGER.info("Auto Delete: " + user.usernameMinecraft + ", "+ user.uuidMinecraft);
                    User jdaUser = jda.getUserById(user.discordID);
                    if(!Objects.isNull(jdaUser)){
                        PrivateChannel privateChannel = jdaUser.openPrivateChannel().complete();
                        WhitelistEmbed embed = new WhitelistEmbed(user.usernameMinecraft, user.uuidMinecraft, user.wildcard, false);
                        privateChannel.sendMessageEmbeds(embed.getEmbed()).queue();
                    }
                    Database.getInstance().removeUserViaUsername(user.usernameMinecraft);
                    MaxWhitelist.getInstance().remove(user.uuidMinecraft, user.usernameMinecraft);
                }
            }
        };

        try {
            jda.awaitReady();
            timer.scheduleAtFixedRate(task, 0, 1000*60*60);
            Objects.requireNonNull(jda.getGuildById(ModConfigs.GUILD_ID))
                    .updateCommands()
                    .addCommands(
                        Commands.slash("deleteuser", "Löscht user auf der Whitelist")
                                .addOption(OptionType.STRING, "username",
                                        "Der Minecraft Username der gelöscht werden soll", true, true)
                    )
                    .addCommands(
                        Commands.slash("listusers", "Listed alle Minecraft whitelisted users")
                    )
                    .queue();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Mod Ready");
	}

}