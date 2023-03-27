package dev.dakoda.dmo.skills.mixin.metalwork;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {

    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"))
    void mixin_onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        ItemStack slotOne = input.getStack(0);
        ItemStack slotTwo = input.getStack(1);

        ItemStack diamondGear = null;
        ItemStack netheriteIngot = null;
        if (slotOne.getItem() == Items.NETHERITE_INGOT) {
            netheriteIngot = slotOne;
            diamondGear = slotTwo;
        } else if (slotTwo.getItem() == Items.NETHERITE_INGOT) {
            netheriteIngot = slotTwo;
            diamondGear = slotOne;
        }

        if (netheriteIngot != null && diamondGear != null) {
            int expGain = DMOSkills.CONFIG.getExp().getMetalwork().getSources().getAction().getUpgradeToNetherite();
            DMOSkills.INSTANCE.gainEXP(player, expGain, Skill.Companion.getMETALWORK());
        }
    }
}
