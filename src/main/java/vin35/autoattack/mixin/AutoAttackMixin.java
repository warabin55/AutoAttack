package vin35.autoattack.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vin35.autoattack.config.AutoAttackConfig;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class AutoAttackMixin {
	@Shadow
	public MultiPlayerGameMode gameMode;
	@Shadow
	public LocalPlayer player;

	// private void continueAttack(boolean leftClick) {
	@Inject(method = "continueAttack(Z)V", at = @At("HEAD"), cancellable = true)
	public void onContinueAttack(boolean isBreakPressed, CallbackInfo info) {
		if (isBreakPressed) {
			if ((player.getMainHandItem().is(ItemTags.SWORDS) && AutoAttackConfig.preventsHittingBlocksSwords) || AutoAttackConfig.preventsHittingBlocks) {
				gameMode.stopDestroyBlock();
				info.cancel();
			}
		}
	}
}
