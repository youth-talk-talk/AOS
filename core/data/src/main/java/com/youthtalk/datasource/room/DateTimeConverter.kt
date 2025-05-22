package com.youthtalk.datasource.room

import androidx.room.TypeConverter
import com.youthtalk.model.post.Post
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.serialization.json.Json

class DateTimeConverter {
    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun localDateToString(value: LocalDate?): String? {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String?): LocalDateTime? {
        return LocalDateTime.parse(value)
    }

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime?): String? {
        return value.toString()
    }

    @TypeConverter
    fun postToString(post: Post?): String {
        return Json.encodeToString(post)
    }

    @TypeConverter
    fun stringToPost(value: String): Post? {
        return Json.decodeFromString<Post?>(value)
    }
}
