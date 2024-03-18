package dev.philtraeger.events;

import com.google.gson.Gson;
import dev.philtraeger.MaxWhitelist;
import dev.philtraeger.Utils;
import dev.philtraeger.config.ModConfigs;
import dev.philtraeger.db.Database;
import dev.philtraeger.embeds.ErrorEmbed;
import dev.philtraeger.embeds.WhitelistEmbed;
import dev.philtraeger.json.MinecraftUser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class MessageReceivedListener implements EventListener {

    public static final Logger LOGGER = LoggerFactory.getLogger("MaxWhitelistMod");

    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        if (!(event instanceof MessageReceivedEvent)) return;

        // create Message and User
        Message message = ((MessageReceivedEvent) event).getMessage();
        User user = ((MessageReceivedEvent) event).getAuthor();

        // is Normal user and Message in Guild and Channel
        if (user.isBot()) return;
        if (!message.hasChannel() && message.hasGuild()) return;
        if (!(message.getChannelId().equals(ModConfigs.GUILD_CHANNEL_ID)
                && Objects.equals(message.getGuildId(), ModConfigs.GUILD_ID))) return;

        // get Member, content, Privat Channel and delete Message
        String content = ((MessageReceivedEvent) event).getMessage().getContentRaw();
        message.delete().complete();
        Member member = ((MessageReceivedEvent) event).getMember();
        PrivateChannel privateChannel = user.openPrivateChannel().complete();

        // has User Role Admin, Mod or Sub
        assert member != null;

        if (Utils.hasNoneRole(member.getRoles())) {
            ErrorEmbed errorEmbed = new ErrorEmbed("Du hast keine Berechtigung dich auf die Whitelist zusetzen!");
            privateChannel.sendMessageEmbeds(errorEmbed.getEmbed()).complete();
            return;   
        }

        String output = null;

        try {
            output = getUrlContents("https://api.ashcon.app/mojang/v2/user/" + URLEncoder.encode(content, StandardCharsets.UTF_8));
        } catch (IOException e) {
            ErrorEmbed errorEmbed = new ErrorEmbed("Der Minecraftname ist Ungültig!");
            privateChannel.sendMessageEmbeds(errorEmbed.getEmbed()).complete();
            return;
        }

        Database db = Database.getInstance();
        MinecraftUser minecraftUser = new Gson().fromJson(output, MinecraftUser.class);
        boolean wildcard = member.getRoles().stream().anyMatch(role -> role.getId().equals(ModConfigs.ADMIN_ID));

        if ((!wildcard) && db.isInDB(member.getUser().getId())){
            ErrorEmbed errorEmbed = new ErrorEmbed("Du bist schon auf der Whitelist!");
            privateChannel.sendMessageEmbeds(errorEmbed.getEmbed()).complete();
            return;
        }

        if (db.isInDBMinecraftUserame(minecraftUser.username)) {
            ErrorEmbed errorEmbed = new ErrorEmbed("Dieser User " + minecraftUser.username + " befindet sich schon auf der Whitelist!");
            privateChannel.sendMessageEmbeds(errorEmbed.getEmbed()).complete();
            return;
        }

        LOGGER.info("DiscordChannel Add: " + minecraftUser.username + ", "+ minecraftUser.uuid);
        db.addUser(member.getUser().getId(), minecraftUser.username, minecraftUser.uuid, wildcard);
        WhitelistEmbed eb = new WhitelistEmbed(minecraftUser.username, minecraftUser.uuid, wildcard, true);
        privateChannel.sendMessageEmbeds(eb.getEmbed()).complete();
        MaxWhitelist.getInstance().add(minecraftUser.uuid, minecraftUser.username);
    }

    private static String getUrlContents(String theUrl) throws IOException {
        StringBuilder content = new StringBuilder();
        // Use try and catch to avoid the exceptions
        URL url = new URL(theUrl); // creating a url object
        URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

        // wrapping the urlconnection in a bufferedreader
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        // reading from the urlconnection using the bufferedreader
        while ((line = bufferedReader.readLine()) != null)
        {
            content.append(line).append("\n");
        }
        bufferedReader.close();

        return content.toString();
    }
}
