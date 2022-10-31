package org.quiltmc.wiki.entity_attributes.mixin

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeInstance
import org.quiltmc.wiki.entity_attributes.AttributesExample
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {

    // @start Add-Attribute-Mixin
    @Inject(method = "createLivingAttributes", at = @At(value = "RETURN"))
    private static void addCustomAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(AttributesExample.GENERIC_JUMP_BOOST)
    }
    // @end Add-Attribute-Mixin

    // @start Use-Attribute
    @Shadow abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute)

    @Inject(method = "getJumpVelocity", at = @At(value = "RETURN"), cancellable = true)
    private void applyJumpBoost(CallbackInfoReturnable<Float> cir) {
        EntityAttributeInstance instance = getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST)
        cir.setReturnValue((cir.getReturnValue() * instance.getValue()) as float)
    }
    // @end Use-Attribute
}
