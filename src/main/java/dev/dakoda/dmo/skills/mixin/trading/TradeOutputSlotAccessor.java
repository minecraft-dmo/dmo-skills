package dev.dakoda.dmo.skills.mixin.trading;

import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.village.Merchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOutputSlot.class)
public interface TradeOutputSlotAccessor {

    @Accessor
    Merchant getMerchant();
}
