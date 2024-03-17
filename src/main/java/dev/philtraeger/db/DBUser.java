package dev.philtraeger.db;

public class DBUser {
    public String discordID;

    public String usernameMinecraft;

    public String uuidMinecraft;

    public boolean wildcard;

    public String getUsernameMinecraft() {
        return usernameMinecraft;
    }

    public String getDiscordID() {
        return discordID;
    }

    public String getUuidMinecraft() {
        return uuidMinecraft;
    }

    public boolean isWildcard() {
        return wildcard;
    }
}
