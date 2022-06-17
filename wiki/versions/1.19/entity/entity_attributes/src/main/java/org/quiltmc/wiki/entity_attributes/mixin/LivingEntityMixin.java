package org.quiltmc.wiki.entity_attributes.mixin;

import org.quiltmc.wiki.entity_attributes.AttributesExample;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	// @start Add-Attribute-Mixin
	@Inject(method = "createLivingAttributes", at = @At(value = "RETURN"))
	private static void addCustomAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(AttributesExample.GENERIC_JUMP_BOOST);
	}
	// @end Add-Attribute-Mixin

	// @start Use-Attribute
	@Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

	@Inject(method = "getJumpVelocity", at = @At(value = "RETURN"), cancellable = true)
	private void applyJumpBoost(CallbackInfoReturnable<Float> cir) {
		EntityAttributeInstance instance = getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST);
		cir.setReturnValue(cir.getReturnValue() * (float) instance.getValue());
	}
	// @end Use-Attribute
}
