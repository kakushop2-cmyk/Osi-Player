package com.nativeplayer.data.model

data class Account(
    val id: String = "default",
    val name: String = "Test Account",
    val domain: String = "",
    val username: String = "",
    val password: String = "",
    val isActive: Boolean = true
) {
    val baseUrl: String
        get() = domain.trimEnd('/')

    val m3uUrl: String
        get() = "$baseUrl/get.php?username=$username&password=$password&type=m3u_plus&output=ts"

    val epgUrl: String
        get() = "$baseUrl/xmltv.php?username=$username&password=$password"

    val apiUrl: String
        get() = "$baseUrl/player_api.php"

    val streamUrl: String
        get() {
            // Build the streaming base URL from server info
            // Format: protocol://domain:port/
            return baseUrl
        }
}
