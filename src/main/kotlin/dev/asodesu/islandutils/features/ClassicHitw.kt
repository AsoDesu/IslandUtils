package dev.asodesu.islandutils.features

import dev.asodesu.islandutils.api.Debounce
import dev.asodesu.islandutils.api.Resources
import dev.asodesu.islandutils.api.game.activeGame
import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.music.resources.handler.MultiDownloadHandler
import dev.asodesu.islandutils.api.sound.SoundEvents
import dev.asodesu.islandutils.api.sound.SoundPlayCallback
import dev.asodesu.islandutils.api.sound.info.SoundInfo
import dev.asodesu.islandutils.api.style
import dev.asodesu.islandutils.api.toSoundEvent
import dev.asodesu.islandutils.games.HoleInTheWall
import dev.asodesu.islandutils.options.ClassicHitwOptions
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import kotlin.time.Duration.Companion.seconds

object ClassicHitw : Module("ClassicHitw") {
    const val MUSIC_ASSET = "classic_hitw/music"
    private val ANNOUCER_ASSETS = listOf(
        "classic_hitw/announcer/archery",
        "classic_hitw/announcer/arrowstorm",
        "classic_hitw/announcer/bombsquad",
        "classic_hitw/announcer/bonuspoints",
        "classic_hitw/announcer/booby",
        "classic_hitw/announcer/bumperbars",
        "classic_hitw/announcer/chickentornado",
        "classic_hitw/announcer/erosion",
        "classic_hitw/announcer/flowerhead",
        "classic_hitw/announcer/gameover",
        "classic_hitw/announcer/gettingdizzy",
        "classic_hitw/announcer/hightide",
        "classic_hitw/announcer/hmm",
        "classic_hitw/announcer/hotcoals",
        "classic_hitw/announcer/jackfrost",
        "classic_hitw/announcer/jungle",
        "classic_hitw/announcer/kaboom",
        "classic_hitw/announcer/letters",
        "classic_hitw/announcer/loser",
        "classic_hitw/announcer/marathon",
        "classic_hitw/announcer/matrix",
        "classic_hitw/announcer/molasses",
        "classic_hitw/announcer/myeyes",
        "classic_hitw/announcer/one",
        "classic_hitw/announcer/plugyourears",
        "classic_hitw/announcer/reproduction",
        "classic_hitw/announcer/revenge",
        "classic_hitw/announcer/solonely",
        "classic_hitw/announcer/stickyshoes",
        "classic_hitw/announcer/superspeed",
        "classic_hitw/announcer/swimmyfish",
        "classic_hitw/announcer/title",
        "classic_hitw/announcer/whatintheblazes",
        "classic_hitw/announcer/whereami",
    )

    private val TRAP_COLOR: TextColor = TextColor.parseColor("#FFA800").orThrow
    private val TRAP_REPLACEMENTS = mutableMapOf(
       "Feeling Hot" to "What in the Blazes",
       "Hot Coals" to "Feeling Hot",
       "Blast-Off" to "Kaboom",
       "Pillagers" to "So Lonely",
       "Leg Day" to "Molasses",
       "Snowball Fight" to "Jack Frost",
    )
    private val GAME_OVER_SOUNDS = listOf(
        "games.global.objective.eliminated",
        "games.global.timer.round_end"
    )
    private val TRAP_SOUND_REGEX = "([ \\-!])".toRegex()

    private val enableAnnoucer by ClassicHitwOptions.annoucer

    override fun init() {
        SoundEvents.SOUND_PLAY.addListener(::handleSound)
    }

    // called from mixin
    fun handleSubtitle(component: Component): Boolean {
        if (!enableAnnoucer) return false
        val isTrap = component.toFlatList().any {
            val style = it.style
            !style.isObfuscated && style.color == TRAP_COLOR
        }
        if (!isTrap) return false

        var trap = component.string
        val replacementTrap = TRAP_REPLACEMENTS[trap]
        if (replacementTrap != null) {
            trap = replacementTrap
            minecraft.gui.setSubtitle(Component.literal(replacementTrap).style { withColor(TRAP_COLOR) })
        }

        try {
            playTrapSound(trap.replace(TRAP_SOUND_REGEX,"").lowercase())
        } catch (e: Exception) {
        }

        return replacementTrap != null
    }

    private val gameOverDebounce = Debounce(1.seconds)
    fun handleSound(info: SoundInfo, ci: SoundPlayCallback.Info) {
        if (activeGame !is HoleInTheWall) return

        // check for round end sound
        val name = info.sound
        if (name.namespace != "mcc") return
        if (!GAME_OVER_SOUNDS.contains(name.path)) return
        if (!gameOverDebounce.consume()) return

        playTrapSound("gameover")
    }

    private fun playTrapSound(trapSoundKey: String) {
        val soundEvent = Resources.islandUtils("classic_hitw.announcer.$trapSoundKey").toSoundEvent()
        minecraft.soundManager.play(SimpleSoundInstance.forUI(soundEvent, 1f, 1f))
    }

    fun downloadAnnoucer() = minecraft.submit { MultiDownloadHandler(ANNOUCER_ASSETS) }
}