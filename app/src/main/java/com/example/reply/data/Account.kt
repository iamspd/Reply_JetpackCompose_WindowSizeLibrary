package com.example.reply.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/** Data class that represents an account. **/

data class Account(
    /** Unique ID of the user **/
    val id: Long,
    @StringRes val firstName: Int,
    @StringRes val lastName: Int,
    @StringRes val email: Int,
    @DrawableRes val avatar: Int
)
