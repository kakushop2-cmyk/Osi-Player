package com.nativeplayer.data.repository

import android.content.SharedPreferences
import com.nativeplayer.data.model.Account
import com.nativeplayer.data.model.AccountSettings
import com.nativeplayer.data.model.PlayerSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val prefs: SharedPreferences
) {
    private val gson = Gson()

    companion object {
        private const val KEY_ACCOUNTS = "accounts"
        private const val KEY_ACTIVE_ACCOUNT = "active_account"
        private const val KEY_M3U_URL = "m3u_url"
        private const val KEY_PLAYER_SETTINGS = "player_settings"
    }

    fun saveAccount(account: Account) {
        val accounts = getAccounts().toMutableList()
        val existingIdx = accounts.indexOfFirst { it.id == account.id }
        if (existingIdx >= 0) {
            accounts[existingIdx] = account
        } else {
            accounts.add(account)
        }
        val json = gson.toJson(accounts)
        prefs.edit().putString(KEY_ACCOUNTS, json).apply()
    }

    fun getAccounts(): List<Account> {
        val json = prefs.getString(KEY_ACCOUNTS, null) ?: return getDefaultAccounts()
        val accounts = try {
            val type = object : TypeToken<List<Account>>() {}.type
            gson.fromJson<List<Account>>(json, type) ?: getDefaultAccounts()
        } catch (e: Exception) {
            getDefaultAccounts()
        }
        // Always override the test_account with latest hardcoded credentials
        val defaults = getDefaultAccounts().associateBy { it.id }
        val merged = accounts.map { account ->
            defaults[account.id]?.let { default ->
                account.copy(
                    username = default.username,
                    password = default.password,
                    domain = default.domain
                )
            } ?: account
        }
        // Save merged accounts back to persist the change
        if (merged != accounts) {
            val json = gson.toJson(merged)
            prefs.edit().putString(KEY_ACCOUNTS, json).apply()
        }
        return merged
    }

    fun deleteAccount(accountId: String) {
        val accounts = getAccounts().filter { it.id != accountId }
        val json = gson.toJson(accounts)
        prefs.edit().putString(KEY_ACCOUNTS, json).apply()
        if (getActiveAccountId() == accountId) {
            setActiveAccount(accounts.firstOrNull()?.id ?: "")
        }
    }

    fun setActiveAccount(accountId: String) {
        prefs.edit().putString(KEY_ACTIVE_ACCOUNT, accountId).apply()
    }

    fun getActiveAccountId(): String {
        return prefs.getString(KEY_ACTIVE_ACCOUNT, null)
            ?: getAccounts().firstOrNull()?.id
            ?: ""
    }

    fun getActiveAccount(): Account? {
        val activeId = getActiveAccountId()
        return getAccounts().find { it.id == activeId }
    }

    fun getDefaultAccounts(): List<Account> {
        return listOf(
            Account(
                id = "test_account",
                name = "Test Account",
                domain = "http://cf.8kdns.cc",
                username = "Fanatik6161",
                password = "d836d6f90d",
                isActive = true
            )
        )
    }

    fun saveM3uUrl(url: String) {
        prefs.edit().putString(KEY_M3U_URL, url).apply()
    }

    fun getM3uUrl(): String {
        return prefs.getString(KEY_M3U_URL, "") ?: ""
    }

    // ── Custom Order Persistence ──────────────────────────────

    /**
     * Saves the user-defined category order for a given type.
     * @param type One of: "live", "movies", "series"
     * @param order The ordered list of category names
     */
    fun saveCategoryOrder(type: String, order: List<String>) {
        prefs.edit().putString("${type}_cat_order", gson.toJson(order)).apply()
    }

    /**
     * Returns the saved category order for [type], or null if none saved yet.
     */
    fun getCategoryOrder(type: String): List<String>? {
        val json = prefs.getString("${type}_cat_order", null) ?: return null
        return try {
            val t = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, t)
        } catch (e: Exception) { null }
    }

    /**
     * Saves the user-defined channel order for a category.
     * Uses a stable hash of the category name as key.
     */
    fun saveChannelOrder(categoryName: String, channelIds: List<String>) {
        val key = "ch_order_${categoryName.hashCode()}"
        prefs.edit().putString(key, gson.toJson(channelIds)).apply()
    }

    /**
     * Returns the saved channel ID order for [categoryName], or null if none saved yet.
     */
    fun getChannelOrder(categoryName: String): List<String>? {
        val key = "ch_order_${categoryName.hashCode()}"
        val json = prefs.getString(key, null) ?: return null
        return try {
            val t = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, t)
        } catch (e: Exception) { null }
    }

    // ── Player Settings ───────────────────────────────────────

    fun getPlayerSettings(): PlayerSettings {
        val json = prefs.getString(KEY_PLAYER_SETTINGS, null) ?: return PlayerSettings()
        return try {
            gson.fromJson(json, PlayerSettings::class.java) ?: PlayerSettings()
        } catch (e: Exception) {
            android.util.Log.e("PreferencesManager", "Error parsing player settings JSON", e)
            PlayerSettings()
        }
    }

    fun savePlayerSettings(settings: PlayerSettings): Boolean {
        return try {
            val json = gson.toJson(settings)
            // WICHTIG: commit() ist synchron und blockiert bis die Änderung auf Disk geschrieben wurde
            // apply() würde asynchron sein und könnte zu Race Conditions führen
            val success = prefs.edit().putString(KEY_PLAYER_SETTINGS, json).commit()
            if (!success) {
                android.util.Log.w("PreferencesManager", "SharedPreferences.commit() returned false")
            }
            success
        } catch (e: Exception) {
            android.util.Log.e("PreferencesManager", "Error saving player settings", e)
            false
        }
    }
}
