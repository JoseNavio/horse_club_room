package me.josena.hipica.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.josena.hipica.data.Booking
import me.josena.hipica.data.BookingProvider

class BookingViewModel : ViewModel() {

    init {
        BookingProvider.bookingModel = this
    }

    //Subscribe to model, it would observe any changes on data
    var bookingsModel = MutableLiveData<MutableList<Booking>>()

    fun setBookingList(list: MutableList<Booking>){
        return bookingsModel.postValue(list)
    }
}