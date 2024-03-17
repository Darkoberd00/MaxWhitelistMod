package dev.philtraeger.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class ErrorEmbed {

    private final EmbedBuilder eb = new EmbedBuilder();

    public ErrorEmbed(String message) {
        eb.setColor(Color.RED);
        eb.setTitle("Ein Fehler ist Aufgetreten!");
        eb.setDescription("Ups!?!");
        eb.addField(new MessageEmbed.Field("Fehlermeldung", message, false));
        eb.addField(new MessageEmbed.Field("Hast du Fragen?",
                "Bei Fragen oder Problemen gerne an ein Mod oder Admin wenden.",
                false));
        eb.setFooter("Credits: Darkoberd00", null);
    }

    public MessageEmbed getEmbed(){
        return eb.build();
    }

}
