package dev.philtraeger.commands;

import dev.philtraeger.db.DBUser;
import dev.philtraeger.db.Database;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class ListUserCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("listusers")) return;

        event.deferReply(true).queue();
        Database db = Database.getInstance();
        List<DBUser> userList = db.getAllUser();

        StringBuilder a = new StringBuilder();
        a.append("## Liste aller gewhitelisted User:\n\n");
        a.append("### Minecraft Username, Minecraft UUID, Discord User\\Wildcard\n");
        if (userList.isEmpty()) a.append("- Die Liste ist leer!");
        for (DBUser user: userList) {
            a.append("- ")
                    .append(user.usernameMinecraft)
                    .append(", ")
                    .append(user.uuidMinecraft)
                    .append(", ");
            if(!user.wildcard){
                a.append("<@").append(user.discordID).append(">");
            } else {
                a.append("Wildcard");
            }
            a.append("\n");
        }
        event.getHook().sendMessage(a.toString()).queue();
    }

}
