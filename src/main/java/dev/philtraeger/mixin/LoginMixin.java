package dev.philtraeger.mixin;

import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginNetworkHandler.class)
public class LoginMixin {

	@Shadow @Final
	MinecraftServer server;

	@Inject(method = "onHello", at = @At(value = "HEAD"))
	private void login(LoginHelloC2SPacket packet, CallbackInfo ci) {
		this.server.getPlayerManager().reloadWhitelist();
	}
}