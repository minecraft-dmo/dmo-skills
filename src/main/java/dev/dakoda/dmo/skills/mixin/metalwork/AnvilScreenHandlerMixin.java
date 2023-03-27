package dev.dakoda.dmo.skills.mixin.metalwork;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import dev.dakoda.dmo.skills.config.DMOConfigMetalwork.Sources.Action;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"))
    void mixin_onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        Action configActions = DMOSkills.CONFIG.getExp().getMetalwork().getSources().getAction();

        ItemStack slotOne = input.getStack(0);
        ItemStack slotTwo = input.getStack(1);

        if (slotOne != ItemStack.EMPTY && slotTwo != ItemStack.EMPTY) {
            if (slotOne.getItem() == slotTwo.getItem()) {
                // Same item - do combine
                int oneHealth = slotOne.getMaxDamage() - slotOne.getDamage();
                int twoHealth = slotTwo.getMaxDamage() - slotTwo.getDamage();
                float oneHealthPercent = (float) oneHealth / slotOne.getMaxDamage();
                float twoHealthPercent = (float) twoHealth / slotTwo.getMaxDamage();
                float averagePercent = (oneHealthPercent + twoHealthPercent) / 2;

                int outputHealth = stack.getMaxDamage() - stack.getDamage();
                float outputPercent = (float) outputHealth / stack.getMaxDamage();

                float maximumEXP = configActions.getRepairMaximumGain();
                if (outputPercent < 1f) {
                    DMOSkills.INSTANCE.gainEXP(player, (int) maximumEXP, Skill.Companion.getMETALWORK());
                } else {
                    float percentDiff = 1f - averagePercent;
                    DMOSkills.INSTANCE.gainEXP(player, (int) (maximumEXP * percentDiff), Skill.Companion.getMETALWORK());
                }
            } else {
                // Different item - do repair
                ItemStack repairing;
                if (slotOne.isDamageable()) repairing = slotOne;
                else if (slotTwo.isDamageable()) repairing = slotTwo;
                else repairing = null;

                if (slotOne.isDamageable() && slotTwo.isDamageable()) repairing = null;

                if (repairing != null) {
                    int health = repairing.getMaxDamage() - repairing.getDamage();
                    if (repairing.getMaxDamage() - health >= repairing.getMaxDamage() / 4) {
                        DMOSkills.INSTANCE.gainEXP(player, configActions.getRepairStandard(), Skill.Companion.getMETALWORK());
                    } else {
                        float multiplier = (float) (repairing.getMaxDamage() - health) / (float) (repairing.getMaxDamage() / 4);
                        DMOSkills.INSTANCE.gainEXP(player, (int) (multiplier * configActions.getRepairStandard()), Skill.Companion.getMETALWORK());
                    }
                }
            }
        } else {
            // Renaming - do nothing
        }
    }
}
