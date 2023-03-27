package dev.dakoda.dmo.skills.mixin.farming;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin {

    @Inject(method = "bondWithPlayer", at = @At("HEAD"))
    void mixin_bondWithPlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            DMOSkills.INSTANCE.gainEXP(player, DMOSkills.CONFIG.getExp().getAnimalCare().getSources().getAction().getTaming(), Skill.Companion.getANIMAL_CARE());
        }
    }
}
