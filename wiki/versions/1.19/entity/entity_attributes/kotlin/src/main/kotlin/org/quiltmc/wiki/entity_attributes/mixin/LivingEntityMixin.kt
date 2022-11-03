@file:JvmName("LivingEntityStaticMixin")
@file:Mixin(LivingEntity::class)

package org.quiltmc.wiki.entity_attributes.mixin

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeInstance
import org.quiltmc.wiki.entity_attributes.GENERIC_JUMP_BOOST
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

// @start Add-Attribute-Mixin
@Inject(method = ["createLivingAttributes"], at = [At(value = "RETURN")])
private fun addCustomAttribute(cir: CallbackInfoReturnable<DefaultAttributeContainer.Builder>) =
    cir.returnValue.add(GENERIC_JUMP_BOOST)
// @end Add-Attribute-Mixin

@Mixin(LivingEntity::class)
abstract class LivingEntityMixin {

    // @start Use-Attribute
    @Shadow
    abstract fun getAttributeInstance(attribute: EntityAttribute): EntityAttributeInstance?

    @Inject(method = ["getJumpVelocity"], at = [At(value = "RETURN")], cancellable = true)
    private fun applyJumpBoost(cir: CallbackInfoReturnable<Float>) {
        getAttributeInstance(GENERIC_JUMP_BOOST)?.run {
            cir.returnValue *= this.value.toFloat()
        }
    }
    // @end Use-Attribute
}
