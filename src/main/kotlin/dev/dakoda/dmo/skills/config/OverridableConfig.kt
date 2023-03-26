package dev.dakoda.dmo.skills.config

/**
 * Marker interface which indicates a config that can be overridden
 * by a mod that interacts with this mod
 */
interface OverridableConfig {
    var overridden: Boolean
}
