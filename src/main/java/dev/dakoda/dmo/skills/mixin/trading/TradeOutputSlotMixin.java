package dev.dakoda.dmo.skills.mixin.trading;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

@Mixin(TradeOutputSlot.class)
abstract class TradeOutputSlotMixin {

    @Shadow @Final private Merchant merchant;

    @Inject(method = "onTakeItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/Merchant;trade(Lnet/minecraft/village/TradeOffer;)V",
            shift = At.Shift.AFTER,
            ordinal = 0
    ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    void mixin_onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci, TradeOffer tradeOffer) {
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            if (merchant instanceof VillagerEntity) {
                int merchantLevel = ((VillagerEntity) merchant).getVillagerData().getLevel();
                ItemStack itemOne = tradeOffer.getOriginalFirstBuyItem();
                ItemStack itemTwo = tradeOffer.getSecondBuyItem();

                int itemOneEXP = itemOne.getCount() / 2;
                if (itemOne.getItem() == Items.EMERALD) itemOneEXP *= 8;

                int itemTwoEXP = itemOne.getCount() / 2;
                if (itemTwo.getItem() == Items.EMERALD) itemTwoEXP *= 8;

                if (itemOne.getItem() == Items.AIR) itemOneEXP *= 0;
                if (itemTwo.getItem() == Items.AIR) itemTwoEXP *= 0;

                float levelMultiplier = switch (merchantLevel) {
                    case 2 -> 1.08f;
                    case 3 -> 1.16f;
                    case 4 -> 1.24f;
                    case 5 -> 1.32f;
                    default -> 1.0f;
                };

                int finalEXP = (int) Math.floor((itemOneEXP * levelMultiplier) + (itemTwoEXP * levelMultiplier));

                DMOSkills.INSTANCE.gainEXP(player, finalEXP, Skill.Companion.getTRADING());
            }
        }
    }
}
