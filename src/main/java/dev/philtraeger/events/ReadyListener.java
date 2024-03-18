package dev.philtraeger.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadyListener implements EventListener {
    public static final Logger LOGGER = LoggerFactory.getLogger("MaxWhitelistMod");

    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        if (event instanceof ReadyEvent)
            LOGGER.info("Bot Ready!");
    }
}
