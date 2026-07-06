package vin35.autoattack;

import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import vin35.autoattack.config.AutoAttackConfig;
import vin35.autoattack.util.UpdateUtil;

public class AutoAttack implements ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	//public static final Logger LOGGER = LoggerFactory.getLogger("autoattack");
	public static String AUTOATTACK_VERSION;
	public static String SERVER_VERSION;
	public static String MINECRAFT_VERSION;
	public static Boolean UPDATE = false;

	@Override
	public void onInitializeClient() {
		//LOGGER.warn("Hello Fabric world!");

		ModContainer autoattack = FabricLoader.getInstance().getModContainer("autoattack")
				.orElseThrow(() -> new IllegalStateException("Couldn't find the mod container for autoattack"));

		AUTOATTACK_VERSION = String.valueOf(autoattack.getMetadata().getVersion());
		MINECRAFT_VERSION = UpdateUtil.getMinecraftVersion();

		JsonObject json = UpdateUtil.getJsonObject("https://raw.githubusercontent.com/warabin55/AutoAttack/updates/updates.json");

		var jsonObj = json.get(MINECRAFT_VERSION);
		if (jsonObj != null) {
			SERVER_VERSION = jsonObj.getAsJsonObject().get("latest").getAsString();
			UPDATE = UpdateUtil.compare(AUTOATTACK_VERSION, SERVER_VERSION) == -1;
		}

		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			//check update
			if (UPDATE && AutoAttackConfig.checkUpdate) {
				if (mc.player != null) {
					mc.player.sendSystemMessage(Component.literal("AutoAttack: new Update Detected! Version: " + SERVER_VERSION));
					UPDATE = false;
				}
			}

			//auto attack
			if ((mc.options.keyAttack.isDown() || AutoAttackConfig.afkAttack) && mc.player != null && mc.level != null && mc.gameMode != null
					&& mc.player.getAttackStrengthScale(0) >= 1) {
				if (mc.hitResult != null) {
					if (mc.hitResult.getType() == HitResult.Type.BLOCK && AutoAttackConfig.cleanCut) {
						BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
						BlockPos blockPos = blockHit.getBlockPos();
						BlockState blockState = mc.level.getBlockState(blockPos);

						if (blockState.getCollisionShape(mc.level, blockPos).isEmpty() || blockState.getDestroySpeed(mc.level, blockPos) == 0.0F) {
							double reach = mc.player.entityInteractionRange();
							Vec3 camera = mc.player.getEyePosition(1.0F);
							Vec3 rotation = mc.player.getViewVector(1.0F);
							Vec3 end = camera.add(rotation.x * reach, rotation.y * reach, rotation.z * reach);
							EntityHitResult result = ProjectileUtil.getEntityHitResult(mc.player, camera, end, new AABB(camera, end), e -> !e.isSpectator() && e.isAttackable(), reach * reach);
							if (result != null && result.getEntity().isAlive()){
								mc.gameMode.attack(mc.player, result.getEntity());
								mc.player.swing(InteractionHand.MAIN_HAND);
							}
						}
					} else if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
						Entity entity = ((EntityHitResult) mc.hitResult).getEntity();
						if (entity.isAlive() && entity.isAttackable()) {
							mc.gameMode.attack(mc.player, entity);
							mc.player.swing(InteractionHand.MAIN_HAND);
						}
					}
				}
			}

			//auto bow
			if (mc.options.keyUse.isDown() && mc.player != null && mc.gameMode != null) {
				ItemStack stack = mc.player.getUseItem();
				Item item = stack.getItem();

				if (item == Items.BOW && AutoAttackConfig.autoBow) {
					float progress = BowItem.getPowerForTime(stack.getUseDuration(mc.player) - mc.player.getUseItemRemainingTicks() - 1);
					if (progress == 1.0F) {
						mc.gameMode.releaseUsingItem(mc.player);
					}
				}

				if (item == Items.CROSSBOW && AutoAttackConfig.autoCrossBow) {
					float progress = (stack.getUseDuration(mc.player) - mc.player.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, mc.player);
					if (progress > 1.0F) {
						mc.gameMode.releaseUsingItem(mc.player);
						mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
					}
				}

				if (item == Items.TRIDENT && AutoAttackConfig.autoTrident) {
					float progress = (stack.getUseDuration(mc.player) - mc.player.getUseItemRemainingTicks()) / 10.0F;
					if (progress > 1.0F) {
						mc.gameMode.releaseUsingItem(mc.player);
					}
				}
			}
		});
	}
}
