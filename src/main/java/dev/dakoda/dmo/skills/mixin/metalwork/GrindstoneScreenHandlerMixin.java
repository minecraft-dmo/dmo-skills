package dev.dakoda.dmo.skills.mixin.metalwork;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import dev.dakoda.dmo.skills.config.DMOConfigMetalwork.Sources.Action;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net/minecraft/screen/GrindstoneScreenHandler$4"})
public class GrindstoneScreenHandlerMixin {

    @Shadow @Final GrindstoneScreenHandler field_16780;

    @Inject(method = "onTakeItem", at = @At("HEAD"))
    void mixin_onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        Action configActions = DMOSkills.CONFIG.getExp().getMetalwork().getSources().getAction();

        Inventory input = ((GrindstoneScreenHandlerAccessor) field_16780).getInput();
        ItemStack slotOne = input.getStack(0);
        ItemStack slotTwo = input.getStack(1);

        if (slotOne != ItemStack.EMPTY && slotTwo != ItemStack.EMPTY) {
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
        } else if (slotOne != ItemStack.EMPTY || slotTwo != ItemStack.EMPTY) {
            ItemStack inputItem;
            if (slotOne != ItemStack.EMPTY) inputItem = slotOne;
            else inputItem = slotTwo;

            if (inputItem.hasEnchantments() && !stack.hasEnchantments()) {
                // Done a dis-enchant
                int exp = configActions.getGrindstoneDisenchant();
                DMOSkills.INSTANCE.gainEXP(player, exp, Skill.Companion.getMETALWORK());
                DMOSkills.INSTANCE.gainEXP(player, exp / 2, Skill.Companion.getENCHANTING());
            }
        }
    }
}
