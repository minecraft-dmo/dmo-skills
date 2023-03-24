package dev.dakoda.dmo.skills.mixin.cooking;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.exp.AbstractCookingChecker.CookingParams;
import dev.dakoda.dmo.skills.exp.CookingChecker;
import dev.dakoda.dmo.skills.exp.data.EXPGain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {

    @Inject(method = "onTakeItem", at = @At("HEAD"))
    void mixin_onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        EXPGain gain = CookingChecker.INSTANCE.resolve(new CookingParams(stack.getItem()), Order.DONT_CARE);
        if (gain != null) DMOSkills.INSTANCE.gainEXP(player, gain);
    }
}
