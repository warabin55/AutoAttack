package vin35.autoattack.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vin35.autoattack.config.AutoAttackConfig;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftPickMixin {

    @Shadow public HitResult hitResult;
    @Shadow public LocalPlayer player;
    @Shadow public ClientLevel level;
    @Shadow public Entity crosshairPickEntity;

    // In 26.x the crosshair picking moved from GameRenderer to Minecraft#pick
    @Inject(method = "pick(F)V", at = @At("TAIL"))
    public void onPick(float partialTicks, CallbackInfo ci) {
        if (this.hitResult != null && this.player != null && this.level != null) {
            if (this.hitResult.getType() == HitResult.Type.BLOCK && AutoAttackConfig.cleanCut) {
                BlockHitResult blockHit = (BlockHitResult) this.hitResult;
                BlockPos blockPos = blockHit.getBlockPos();
                BlockState blockState = this.level.getBlockState(blockPos);

                if (blockState.getCollisionShape(this.level, blockPos).isEmpty() || blockState.getDestroySpeed(this.level, blockPos) == 0.0F) {
                    double reach = this.player.entityInteractionRange();
                    Vec3 camera = this.player.getEyePosition(1.0F);
                    Vec3 rotation = this.player.getViewVector(1.0F);
                    Vec3 end = camera.add(rotation.x * reach, rotation.y * reach, rotation.z * reach);
                    EntityHitResult result = ProjectileUtil.getEntityHitResult(this.player, camera, end, new AABB(camera, end), e -> !e.isSpectator() && e.isAttackable(), reach * reach);
                    if (result != null && result.getEntity().isAlive()){
                        this.crosshairPickEntity = result.getEntity();
                    }
                }
            }
        }
    }
}
