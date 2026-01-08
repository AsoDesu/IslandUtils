package dev.asodesu.islandutils.api.extentions

import dev.asodesu.islandutils.IslandUtils
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun String.calculateHash(messageDigest: MessageDigest): String {
    return try {
        messageDigest.update(this.toByteArray(StandardCharsets.UTF_8))
        val digest = messageDigest.digest()
        "%064x".format(BigInteger(1, digest))
    } catch (e: Exception) {
        IslandUtils.logger.error("Failed to calculate ${messageDigest.algorithm} hash for $this", e)
        ""
    }
}

fun String.calculateSha256(): String {
    return this.calculateHash(MessageDigest.getInstance("SHA-256"))
}