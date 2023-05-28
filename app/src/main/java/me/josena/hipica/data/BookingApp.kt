package me.josena.hipica.data

import android.app.Application
import androidx.room.Room

class BookingApp : Application() {

    val room = Room.databaseBuilder(this, SQLiteBookingDB::class.java, "booking_room_db")
}