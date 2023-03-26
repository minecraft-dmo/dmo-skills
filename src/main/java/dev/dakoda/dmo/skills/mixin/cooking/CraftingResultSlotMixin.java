package dev.dakoda.dmo.skills.mixin.cooking;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.exp.AbstractCookingChecker.CookingParams;
import dev.dakoda.dmo.skills.exp.CookingChecker;
import dev.dakoda.dmo.skills.exp.data.EXPGain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.dakoda.dmo.skills.exp.map.EXPMap.Entry.Settings.Order;

@SuppressWarnings("DuplicatedCode")
@Mixin(CraftingResultSlot.class)
abstract class CraftingResultSlotMixin extends Slot {

    @Shadow @Final private PlayerEntity player;

    public CraftingResultSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    /*
        Injects code to gain Cooking EXP when an item is crafted using quick transfer.
     */
    @Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;I)V", at = @At("HEAD"))
    void onCrafted(ItemStack stack, int amount, CallbackInfo ci) {
        if (!DMOSkills.CONFIG.getExp().getCooking().getOverridden()) {
            if (!player.isCreative() && !player.isSpectator()) {
                EXPGain gain = CookingChecker.INSTANCE.resolve(new CookingParams(stack.getItem()), Order.DONT_CARE);
                if (gain != null) DMOSkills.INSTANCE.gainEXP(player, gain);
            }
        }
    }

    /*
        Injects code to gain Cooking EXP when an item is crafted by manually taking it out of the output slot.
     */
    @Inject(method = "onTakeItem", at = @At("HEAD"))
    void mixin_onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (!DMOSkills.CONFIG.getExp().getCooking().getOverridden()) {
            if (!player.isCreative() && !player.isSpectator()) {
                EXPGain gain = CookingChecker.INSTANCE.resolve(new CookingParams(stack.getItem()), Order.DONT_CARE);
                if (gain != null) DMOSkills.INSTANCE.gainEXP(player, gain);
            }
        }
    }
}
