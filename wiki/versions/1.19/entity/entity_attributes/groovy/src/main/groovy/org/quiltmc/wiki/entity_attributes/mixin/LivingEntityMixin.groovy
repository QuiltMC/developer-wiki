package org.quiltmc.wiki.entity_attributes.mixin

import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import org.quiltmc.wiki.entity_attributes.AttributesExample
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@POJO
@CompileStatic
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {

    // @start Add-Attribute-Mixin
    @Inject(method = 'createLivingAttributes', at = @At(value = 'RETURN'))
    private static void addCustomAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.returnValue.add(AttributesExample.GENERIC_JUMP_BOOST)
    }
    // @end Add-Attribute-Mixin

    // @start Use-Attribute
    @Shadow abstract AttributeInstance getAttribute(Attribute attribute)

    @Inject(method = 'getJumpPower', at = @At(value = 'RETURN'), cancellable = true)
    private void applyJumpBoost(CallbackInfoReturnable<Float> cir) {
        AttributeInstance instance = getAttribute(AttributesExample.GENERIC_JUMP_BOOST)
        cir.returnValue = (cir.returnValue * instance.value).toFloat()
    }
    // @end Use-Attribute
}
