package dev.philtraeger.commands;

import dev.philtraeger.db.DBUser;
import dev.philtraeger.db.Database;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoComplete extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (!(event.getName().equals("deleteuser")
                && event.getFocusedOption().getName().equals("username"))) return;

        Database db = Database.getInstance();
        ArrayList<DBUser> users = db.getAllUser();
        String[] usernames = users.stream()
                .map(DBUser::getUsernameMinecraft)
                .toArray(String[]::new);
        List<Command.Choice> options = Stream.of(usernames)
                .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                .map(word -> new Command.Choice(word, word)) // map the words to choices
                .collect(Collectors.toList());

        event.replyChoices(options).queue();
    }
}
