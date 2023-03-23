package dev.dakoda.dmo.skills.component

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy.ALWAYS_COPY
import net.minecraft.util.Identifier

class DMOSkillsComponents : EntityComponentInitializer {

    companion object {
        val COMP_SKILLS_EXP: ComponentKey<ISkillsEXPComponent> = ComponentRegistry.getOrCreate(
            Identifier("dmo", "skills_exp"), ISkillsEXPComponent::class.java
        )
        val COMP_SKILLS_TRACKED: ComponentKey<ISkillsTrackedComponent> = ComponentRegistry.getOrCreate(
            Identifier("dmo", "skills_track"), ISkillsTrackedComponent::class.java
        )
        val COMP_SKILLS_DISCOVERED: ComponentKey<ISkillsDiscoveredComponent> = ComponentRegistry.getOrCreate(
            Identifier("dmo", "skills_discovered"), ISkillsDiscoveredComponent::class.java
        )
    }

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(COMP_SKILLS_EXP, { SkillsEXPComponent() }, ALWAYS_COPY)
        registry.registerForPlayers(COMP_SKILLS_TRACKED, { SkillsTrackedComponent() }, ALWAYS_COPY)
        registry.registerForPlayers(COMP_SKILLS_DISCOVERED, { SkillsDiscoveredComponent() }, ALWAYS_COPY)
    }
}
