package dev.dakoda.dmo.skills.mixin.trading;

import net.minecraft.item.Item;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffers.BuyForOneEmeraldFactory.class)
public interface BuyForOneEmeraldFactoryAccessor {

    @Accessor
    Item getBuy();
}
