package com.entremp.core.entremp.persistence.chat

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object MessageTable: UUIDTable(name = "message") {
    val chatId: Column<UUID> = uuid("chat_id")
    val senderId: Column<UUID> = uuid("sender_id")
    val receiverId: Column<UUID> = uuid("receiver_id")
    val message: Column<String> = varchar(
        name = "message",
        length = 600
    )
    val sent: Column<DateTime> = datetime("sent")
    val seen: Column<Boolean> = bool("seen")
    val createdAt: Column<DateTime> = datetime("created_at")
    val updatedAt: Column<DateTime?> = date("updated_at").nullable()
}