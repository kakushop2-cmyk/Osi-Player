package com.nativeplayer.data.parser

import com.nativeplayer.data.model.Channel

object M3uParser {

    fun parse(content: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines()

        var currentName: String? = null
        var currentLogo: String? = null
        var currentGroup: String = "All"
        var currentEpgId: String? = null

        for (line in lines) {
            when {
                line.startsWith("#EXTINF:") -> {
                    currentName = extractAttribute(line, "tvg-name")
                        ?: line.substringAfter(",").trim()
                        ?: "Unknown"
                    currentLogo = extractAttribute(line, "tvg-logo")
                    currentGroup = extractAttribute(line, "group-title") ?: "All"
                    currentEpgId = extractAttribute(line, "tvg-id")
                }
                line.startsWith("http") || line.startsWith("rtmp") || line.startsWith("rtsp") -> {
                    val name = currentName ?: "Channel ${channels.size + 1}"
                    channels.add(
                        Channel(
                            id = "ch_${channels.size}",
                            name = name,
                            url = line.trim(),
                            logo = currentLogo,
                            group = currentGroup,
                            epgId = currentEpgId
                        )
                    )
                    currentName = null
                    currentLogo = null
                    currentGroup = "All"
                    currentEpgId = null
                }
            }
        }
        return channels
    }

    fun groupByCategory(channels: List<Channel>): List<com.nativeplayer.data.model.Category> {
        return channels.groupBy { it.group }
            .map { (group, chs) ->
                com.nativeplayer.data.model.Category(name = group, channels = chs)
            }
    }

    private fun extractAttribute(line: String, attribute: String): String? {
        val regex = Regex("""${Regex.escape(attribute)}="([^"]*)"""")
        return regex.find(line)?.groupValues?.getOrNull(1)
    }
}
