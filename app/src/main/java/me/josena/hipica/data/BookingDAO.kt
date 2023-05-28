package me.josena.hipica.data

import androidx.lifecycle.LiveData
import androidx.room.*
import org.jetbrains.annotations.NotNull

@Dao
interface BookingDAO {
    @Query("SELECT * FROM bookings ORDER BY date DESC, hour DESC")
    suspend fun getAllBookings(): MutableList<Booking>

    @Query("SELECT * FROM bookings WHERE date = :date AND hour = :hour ORDER BY date DESC, hour DESC")
    fun getBookingsByDateAndHour(date: String, hour: String): MutableList<Booking>

    @Query("UPDATE bookings SET horse = :horse, tlf = :tlf, comment = :comment WHERE rider = :rider")
    suspend fun updateBookingDetails(
        rider: String,
        horse: String,
        tlf: String,
        comment: String
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookings: MutableList<Booking>)

    @Delete
    suspend fun deleteBooking(booking: Booking)

    @Query("DELETE FROM bookings")
    suspend fun deleteAllBookings()
}