package dev.dakoda.dmo.skills.mixin.dungeoneer;

import dev.dakoda.dmo.skills.ModHelper;
import dev.dakoda.dmo.skills.Skill;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin {

    @Shadow
    @Nullable
    public abstract Identifier getLootTableId();

    /*
        Injects code to gain Dungeoneer EXP when the chest minecart is destroyed before being opened
        for the first time.
     */
    @Inject(method = "dropItems", at = @At(value = "HEAD"))
    void mixin_dropItems(DamageSource damageSource, CallbackInfo callbackInfo) {
        Entity attacker = damageSource.getAttacker();
        if (attacker instanceof PlayerEntity && getLootTableId() != null) {
            ModHelper.INSTANCE.gainEXP(
                    (PlayerEntity) attacker,
                    ModHelper.INSTANCE.getCONFIG().getExp().getDungeoneer().getSources().getBreak_().getMinecartChest(),
                    Skill.Companion.getDUNGEONEER()
            );
        }
    }

    /*
        Injects code to gain Dungeoneer EXP when the chest minecart is first opened.
     */
    @Inject(
            method = "createMenu", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/vehicle/StorageMinecartEntity;generateInventoryLoot(Lnet/minecraft/entity/player/PlayerEntity;)V",
            shift = At.Shift.BEFORE
    ))
    void mixin_createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity, CallbackInfoReturnable<ScreenHandler> cir) {
        if (playerEntity != null && getLootTableId() != null) {
            ModHelper.INSTANCE.gainEXP(
                    playerEntity,
                    ModHelper.CONFIG.getExp().getDungeoneer().getSources().getUse().getMinecartChest(),
                    Skill.Companion.getDUNGEONEER()
            );
        }
    }
}
