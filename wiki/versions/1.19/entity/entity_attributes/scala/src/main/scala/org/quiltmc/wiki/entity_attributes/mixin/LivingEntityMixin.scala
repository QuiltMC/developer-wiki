package org.quiltmc.wiki.entity_attributes.mixin

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.{DefaultAttributeContainer, EntityAttribute, EntityAttributeInstance}
import org.quiltmc.wiki.entity_attributes.AttributesExample
import org.spongepowered.asm.mixin.injection.{At, Inject}
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.spongepowered.asm.mixin.{Mixin, Shadow}

import scala.annotation.static

@Mixin(Array(classOf[LivingEntity]))
abstract class LivingEntityMixin {

    // @start Use-Attribute
    @Shadow
    def getAttributeInstance(attribute: EntityAttribute): EntityAttributeInstance

    @Inject(method = Array("getJumpVelocity"), at = Array(At(value = "RETURN")), cancellable = true)
    private def applyJumpBoost(cir: CallbackInfoReturnable[Float]): Unit = Option(
        getAttributeInstance(AttributesExample.GENERIC_JUMP_BOOST)
    ).foreach { instance =>
        cir.setReturnValue(cir.getReturnValue * instance.getValue.toFloat)
    }
    // @end Use-Attribute
}
object LivingEntityMixin {

    // @start Add-Attribute-Mixin
    @static
    @Inject(method = Array("createLivingAttributes"), at = Array(At(value = "RETURN")))
    private def addCustomAttribute(cir: CallbackInfoReturnable[DefaultAttributeContainer.Builder]): Unit =
        cir.getReturnValue.add(AttributesExample.GENERIC_JUMP_BOOST)
    // @end Add-Attribute-Mixin
}
