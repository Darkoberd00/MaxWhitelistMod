package dev.philtraeger.commands;

import dev.philtraeger.MaxWhitelist;
import dev.philtraeger.db.DBUser;
import dev.philtraeger.db.Database;
import dev.philtraeger.embeds.ErrorEmbed;
import dev.philtraeger.embeds.SuccessEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DeleteUserCommand extends ListenerAdapter {
    public static final Logger LOGGER = LoggerFactory.getLogger("MaxWhitelistMod");

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("deleteuser")) return;
        event.deferReply(true).queue();

        OptionMapping option = event.getOption("username");

        if (Objects.isNull(option)) {
            ErrorEmbed errorEmbed = new ErrorEmbed("Du musst ein Username angeben!");
            event.getHook().sendMessageEmbeds(errorEmbed.getEmbed()).queue();
            return;
        }

        String username = option.getAsString();
        Database db = Database.getInstance();

        if (!db.isInDBMinecraftUserame(username)) {
            ErrorEmbed errorEmbed = new ErrorEmbed("Der Minecraft Username ist nicht auf der Whitelist: " + username);
            event.getHook().sendMessageEmbeds(errorEmbed.getEmbed()).queue();
            return;
        }

        DBUser user = db.getUser(username);

        LOGGER.info("Command Delete: " + user.usernameMinecraft + ", "+ user.uuidMinecraft);
        MaxWhitelist.getInstance().remove(user.uuidMinecraft, user.usernameMinecraft);
        db.removeUserViaUsername(username);
        SuccessEmbed successEmbed = new SuccessEmbed(
                "Der Minecraft Username wurde von der Whitelist gestrichen: " + username,
                "https://mc-heads.net/avatar/" + username);
        event.getHook().sendMessageEmbeds(successEmbed.getEmbed()).queue();
    }
}
