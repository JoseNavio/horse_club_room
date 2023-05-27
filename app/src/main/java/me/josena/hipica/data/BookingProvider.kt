package me.josena.hipica.data

import me.josena.hipica.viewmodel.BookingViewModel

class BookingProvider {

    companion object {

        var bookingModel :  BookingViewModel? = null
//        private val bookings = mutableListOf<Booking>()

        private val bookings = mutableListOf<Booking>(
            Booking("Juan Garrido", "Cipote", "666112233", "29/05/2023", "19:00", ""),
            Booking("Lucia Pomodoro", "Reina", "666112234", "30/05/2023", "18:00", "")
        )
        //Add a booking
        fun addBooking(booking: Booking){
            bookings.add(booking)
        }
    }
}