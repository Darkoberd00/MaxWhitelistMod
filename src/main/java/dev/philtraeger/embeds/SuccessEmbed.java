package dev.philtraeger.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class SuccessEmbed {

    private final EmbedBuilder eb = new EmbedBuilder();

    public SuccessEmbed(String message){
        eb.setColor(Color.GREEN);
        eb.setTitle("Der Command wurde erfolgreich ausgeführt");
        eb.addField(new MessageEmbed.Field("Message", message, false));
        eb.addField(new MessageEmbed.Field("Hast du Fragen?",
                "Bei Fragen oder Problemen gerne an ein Mod oder Admin wenden.",
                false));
        eb.setFooter("Credits: Darkoberd00", null);
    }

    public SuccessEmbed(String message, String image) {
        this(message);
        eb.setImage(image);
    }

    public MessageEmbed getEmbed(){
        return eb.build();
    }

}
