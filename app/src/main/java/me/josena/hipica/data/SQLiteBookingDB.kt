package me.josena.hipica.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Booking::class],
    version = 1,
    exportSchema = false
)
abstract class SQLiteBookingDB : RoomDatabase() {
    abstract fun bookingDAO(): BookingDAO
}