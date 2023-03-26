package dev.dakoda.dmo.skills.mixin.alchemy;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerEntityMixin {

    @Shadow
    private @Nullable UUID converter;

    @Inject(method = "finishConversion", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;handleInteraction(Lnet/minecraft/entity/EntityInteraction;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/InteractionObserver;)V"
    ))
    void mixin_finishConversion(ServerWorld world, CallbackInfo ci) {
        PlayerEntity playerEntity = world.getPlayerByUuid(converter);
        if (playerEntity != null) {
            DMOSkills.INSTANCE.gainEXP(
                    playerEntity,
                    DMOSkills.CONFIG.getExp().getAlchemy().getSources().getAction().getCureZombieVillager(),
                    Skill.Companion.getALCHEMY()
            );
        }
    }
}
