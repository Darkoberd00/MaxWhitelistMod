package dev.philtraeger.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class WhitelistEmbed {

    private final EmbedBuilder eb = new EmbedBuilder();

    public WhitelistEmbed(String username, String uuid, boolean wildcard, boolean added){
        eb.setColor(added ? Color.GREEN : Color.RED);
        eb.setTitle(added ? "Willkommen auf dem Community Server!" : "Danke das du auf dem Community Server warst!", null);
        eb.setDescription(added ? "Du wurdest in die Whitelist aufgenommen!" : "Du wurdest von der Whitelist gelöscht!");
        if (wildcard) {
            eb.addField(new MessageEmbed.Field("Wildcard",
                    "Dieser user wurde über das Wildcard-System hinzugefügt", false));
        }
        eb.addField(new MessageEmbed.Field("UUID", uuid, false));
        eb.addField(new MessageEmbed.Field("Username", username, false));
        eb.addField(new MessageEmbed.Field("Hast du Fragen?",
                "Bei Fragen oder Problemen gerne an ein Mod oder Admin wenden.",
                false));
        eb.setImage("https://mc-heads.net/avatar/" + username);
        eb.setFooter("Credits: Darkoberd00", null);
    }

    public MessageEmbed getEmbed(){
        return eb.build();
    }

}
