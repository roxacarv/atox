package com.atox.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class ConversorDeTipo {

    @TypeConverter
    public static Date toDate(Long value) {
        if(value == null)
            return null;
        else
            return new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        if(value == null)
            return null;
        else
            return value.getTime();
    }

}
