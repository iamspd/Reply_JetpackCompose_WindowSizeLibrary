package com.example.reply.data

import androidx.annotation.StringRes

/** Data class that represents an Email object **/

data class Email(
    /** Unique ID of the email **/
    val id: Long,
    val sender: Account,
    val recipients: List<Account> = emptyList(),
    @StringRes val subject: Int = -1,
    @StringRes val body: Int = -1,
    var mailbox: MailboxType = MailboxType.Inbox,

    /**
     * Relative duration in which it was created. (e.g. 20 mins. ago)
     * For now, it is hardcoded to a [String] value.
     */
    var createdAt: Int = -1
)
