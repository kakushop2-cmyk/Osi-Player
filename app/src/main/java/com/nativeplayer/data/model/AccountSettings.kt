package com.nativeplayer.data.model

data class AccountSettings(
    val accounts: List<Account> = emptyList(),
    val activeAccountId: String = ""
)
