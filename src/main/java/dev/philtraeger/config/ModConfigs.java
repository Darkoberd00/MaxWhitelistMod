package dev.philtraeger.config;

import com.mojang.datafixers.util.Pair;
import dev.philtraeger.MaxWhitelistMod;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static String BOT_TOKEN;
    public static String GUILD_ID;
    public static String GUILD_CHANNEL_ID;
    public static String ADMIN_ID;
    public static String MOD_ID;
    public static String WHITELISTED_ID;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of("max-whitelistmod-config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("discord.bot.token", "TOKEN"), "String");
        configs.addKeyValuePair(new Pair<>("discord.guild.id", "GuildID"), "String");
        configs.addKeyValuePair(new Pair<>("discord.guild.channel", "GuildChannelID"), "String");
        configs.addKeyValuePair(new Pair<>("discord.role.admin", "AdminID"), "String");
        configs.addKeyValuePair(new Pair<>("discord.role.mod", "ModID"), "String");
        configs.addKeyValuePair(new Pair<>("discord.role.whitelisted", "WhitelistedID"), "String");
    }

    private static void assignConfigs() {
        BOT_TOKEN = CONFIG.getOrDefault("discord.bot.token", null);
        GUILD_ID = CONFIG.getOrDefault("discord.guild.id", null);
        GUILD_CHANNEL_ID = CONFIG.getOrDefault("discord.guild.channel", null);
        ADMIN_ID = CONFIG.getOrDefault("discord.role.admin", null);
        MOD_ID = CONFIG.getOrDefault("discord.role.mod", null);
        WHITELISTED_ID = CONFIG.getOrDefault("discord.role.whitelisted", null);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}
