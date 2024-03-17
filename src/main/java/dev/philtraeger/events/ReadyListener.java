package dev.philtraeger.events;

import dev.philtraeger.MaxWhitelist;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ReadyListener implements EventListener {
    private static final Logger logger = LogManager.getLogger(MaxWhitelist.class);

    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        if (event instanceof ReadyEvent)
            logger.info("Bot Ready!");
    }
}
