package org.quiltmc.wiki.entity_attributes

import com.mojang.brigadier.CommandDispatcher
import groovy.transform.CompileStatic
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

@CompileStatic
class AttributesExample implements ModInitializer {

    // @start Attribute-Instance
    static final EntityAttribute GENERIC_JUMP_BOOST = new ClampedEntityAttribute('attribute.name.generic_jump_boost', 1.0d, 0.0d, 2.0d).setTracked(true)
    // @end Attribute-Instance

    // @start Modifier-Instance
    static final UUID SOME_MODIFIER_ID = UUID.fromString('de17612a-adec-11ec-b909-0242ac120002')

    static final EntityAttributeModifier SOME_MODIFIER = new EntityAttributeModifier(
            SOME_MODIFIER_ID,
            'Jump stick modifier',
            0.5d,
            EntityAttributeModifier.Operation.ADDITION
    )
    // @end Modifier-Instance

    static final JumpStick JUMP_STICK = new JumpStick(new QuiltItemSettings().group(ItemGroup.TOOLS))

    @Override
    void onInitialize(ModContainer mod) {
        def modId = mod.metadata().id()

        // @start Register-Attribute
        Registry.ATTRIBUTE[new Identifier(modId, 'generic.jump_boost')] = GENERIC_JUMP_BOOST
        // @end Register-Attribute
        Registry.ITEM[new Identifier(modId, 'jump_stick')] = JUMP_STICK
        CommandRegistrationCallback.EVENT << ({ CommandDispatcher dispatcher, buildContext, environment ->
            JumpBoostCommand.register(dispatcher)
        } as CommandRegistrationCallback)
    }
}
