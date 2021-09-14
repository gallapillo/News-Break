package com.gallapillo.newsbreak.model.db

import androidx.room.TypeConverter
import com.gallapillo.newsbreak.model.Source


/* Ковертер для модели source */
class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}