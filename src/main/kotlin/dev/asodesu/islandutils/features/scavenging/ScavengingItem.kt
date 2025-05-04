package dev.asodesu.islandutils.features.scavenging

import net.minecraft.network.chat.Component

class ScavengingItem(val rewards: MutableList<Reward> = mutableListOf()) {
    class Reward(val id: String, val component: Component, val total: Int)
}