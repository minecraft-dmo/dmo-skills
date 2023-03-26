package dev.dakoda.dmo.skills.mixin.farming;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(AnimalEntity.class)
public class AnimalEntityMixin {

    @Shadow private @Nullable UUID lovingPlayer;

    @Inject(method = "lovePlayer", at = @At("HEAD"))
    void mixin_lovePlayer(PlayerEntity player, CallbackInfo ci) {
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            DMOSkills.INSTANCE.gainEXP(player, 5, Skill.Companion.getANIMAL_CARE());
        }
    }

    @Inject(method = "breed", at = @At("HEAD"))
    void mixin_breed(ServerWorld world, AnimalEntity other, CallbackInfo ci) {
        if (lovingPlayer != null) {
            PlayerEntity playerOne = world.getPlayerByUuid(lovingPlayer);
            if (playerOne != null && !playerOne.isCreative() && !playerOne.isSpectator()) {
                DMOSkills.INSTANCE.gainEXP(playerOne, 15, Skill.Companion.getANIMAL_CARE());
            }
        }
        if (other.getLovingPlayer() != null) {
            PlayerEntity playerTwo = world.getPlayerByUuid(lovingPlayer);
            if (playerTwo != null && !playerTwo.isCreative() && !playerTwo.isSpectator()) {
                DMOSkills.INSTANCE.gainEXP(playerTwo, 15, Skill.Companion.getANIMAL_CARE());
            }
        }
    }
}
