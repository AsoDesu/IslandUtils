package dev.asodesu.islandutils.features

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.music.resources.handler.MultiDownloadHandler
import dev.asodesu.islandutils.options.ClassicHitwOptions

object ClassicHitw : Module("ClassicHitw") {
    val MUSIC_ASSET = "classic_hitw/music"
    val ANNOUCER_ASSETS = listOf(
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

    private val enableAnnoucer by ClassicHitwOptions.annoucer
    private val enableMusic by ClassicHitwOptions.music

    override fun init() {
    }

    fun downloadAnnoucer() = minecraft.submit { MultiDownloadHandler(ANNOUCER_ASSETS) }
}