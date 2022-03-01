package dev.dakoda.dmo.skills.component

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy.ALWAYS_COPY
import net.minecraft.util.Identifier

class DMOSkillsComponents : EntityComponentInitializer {

    companion object {
        val COMPONENT_PLAYER_SKILLS: ComponentKey<IPlayerSkillsComponent> = ComponentRegistry.getOrCreate(
            Identifier("dmo", "skills"), IPlayerSkillsComponent::class.java
        )
    }

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(COMPONENT_PLAYER_SKILLS, { PlayerSkillsComponent() }, ALWAYS_COPY)
    }
}