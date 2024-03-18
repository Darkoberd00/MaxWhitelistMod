package dev.philtraeger.mixin;

import com.mojang.authlib.GameProfile;
import dev.philtraeger.db.Database;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.WhitelistCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(WhitelistCommand.class)
public abstract class WhitelistMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("MaxWhitelistMod");

    @Inject(method = "executeAdd", at = @At(value = "HEAD"))
    private static void addWhitelist(ServerCommandSource source, Collection<GameProfile> targets, CallbackInfoReturnable<Integer> cir) {
        source.getServer().getPlayerManager().reloadWhitelist();
        for (GameProfile profile: targets) {
            LOGGER.info("InGameCommand Add: " + profile.getName() + ", "+ profile.getId().toString());
            Database.getInstance().addUser("0", profile.getName(), profile.getId().toString(),true);
        }
    }

    @Inject(method = "executeRemove", at = @At(value = "HEAD"))
    private static void removeWhitelist(ServerCommandSource source, Collection<GameProfile> targets, CallbackInfoReturnable<Integer> cir) {
        source.getServer().getPlayerManager().reloadWhitelist();
        for (GameProfile profile: targets) {
            LOGGER.info("InGameCommand Delete: " + profile.getName() + ", "+ profile.getId().toString());
            Database.getInstance().removeUserViaUsername(profile.getName());
        }
    }

    @Inject(method = "executeList", at = @At(value = "HEAD"))
    private static void listWhitelist(ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
        source.getServer().getPlayerManager().reloadWhitelist();
    }

}
