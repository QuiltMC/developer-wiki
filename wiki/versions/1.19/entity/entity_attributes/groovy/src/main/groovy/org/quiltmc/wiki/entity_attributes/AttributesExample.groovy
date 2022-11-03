package org.quiltmc.wiki.entity_attributes

import com.mojang.brigadier.CommandDispatcher
import groovy.transform.CompileStatic
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.minecraft.world.item.CreativeModeTab
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

@CompileStatic
class AttributesExample implements ModInitializer {

    // @start Attribute-Instance
    static final Attribute GENERIC_JUMP_BOOST = new RangedAttribute('attribute.name.generic_jump_boost', 1.0d, 0.0d, 2.0d).setSyncable(true)
    // @end Attribute-Instance

    // @start Modifier-Instance
    static final UUID SOME_MODIFIER_ID = UUID.fromString('de17612a-adec-11ec-b909-0242ac120002')

    static final AttributeModifier SOME_MODIFIER = new AttributeModifier(
            SOME_MODIFIER_ID,
            'Jump stick modifier',
            0.5d,
            AttributeModifier.Operation.ADDITION
    )
    // @end Modifier-Instance

    static final JumpStick JUMP_STICK = new JumpStick(new QuiltItemSettings().group(CreativeModeTab.TAB_TOOLS))

    @Override
    void onInitialize(ModContainer mod) {
        def modId = mod.metadata().id()

        // @start Register-Attribute
        Registry.ATTRIBUTE[new ResourceLocation(modId, 'generic.jump_boost')] = GENERIC_JUMP_BOOST
        // @end Register-Attribute
        Registry.ITEM[new ResourceLocation(modId, 'jump_stick')] = JUMP_STICK
        CommandRegistrationCallback.EVENT << ({ CommandDispatcher dispatcher, buildContext, environment ->
            JumpBoostCommand.register(dispatcher)
        } as CommandRegistrationCallback)
    }
}
