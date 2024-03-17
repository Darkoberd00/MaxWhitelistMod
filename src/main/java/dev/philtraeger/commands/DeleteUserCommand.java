package dev.philtraeger.commands;

import dev.philtraeger.MaxWhitelist;
import dev.philtraeger.db.DBUser;
import dev.philtraeger.db.Database;
import dev.philtraeger.embeds.ErrorEmbed;
import dev.philtraeger.embeds.SuccessEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;

public class DeleteUserCommand extends ListenerAdapter {
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

        System.out.println(user.uuidMinecraft);
        System.out.println(user.usernameMinecraft);
        MaxWhitelist.getInstance().remove(user.uuidMinecraft, user.usernameMinecraft);
        db.removeUserViaUsername(username);
        SuccessEmbed successEmbed = new SuccessEmbed(
                "Der Minecraft Username wurde von der Whitelist gestrichen: " + username,
                "https://mc-heads.net/avatar/" + username);
        event.getHook().sendMessageEmbeds(successEmbed.getEmbed()).queue();
    }
}
