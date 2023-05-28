package me.josena.hipica.repository

import me.josena.hipica.data.Booking

interface BookingRepository {
    suspend fun getAllBookings(): List<Booking>
    suspend fun insertBooking(booking: Booking)
    suspend fun deleteBooking(booking: Booking)
}