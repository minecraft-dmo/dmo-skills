package dev.dakoda.dmo.skills.mixin.enchanting;

import dev.dakoda.dmo.skills.DMOSkills;
import dev.dakoda.dmo.skills.Skill;
import dev.dakoda.dmo.skills.config.DMOConfigEnchanting.Sources.Action;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {

    @Inject(method = "method_17410", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V"
    ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    void mixin_onButtonClick(
            ItemStack input, int enchantmentCost,
            PlayerEntity player, int j,
            ItemStack lapis, World world,
            BlockPos pos, CallbackInfo ci,
            ItemStack output
    ) {
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            Action exp = DMOSkills.CONFIG.getExp().getEnchanting().getSources().getAction();
            int gain = switch(enchantmentCost + 1) {
                case 1 -> exp.getEnchantmentCostOne();
                case 2 -> exp.getEnchantmentCostTwo();
                case 3 -> exp.getEnchantmentCostThree();
                default -> 0;
            };
            DMOSkills.INSTANCE.gainEXP(player, gain, Skill.Companion.getENCHANTING());
        }
    }
}
