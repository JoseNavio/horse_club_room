package me.josena.hipica.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.josena.hipica.data.Booking
import me.josena.hipica.data.BookingProvider
import me.josena.hipica.data.SQLiteBookingDB

class BookingViewModel : ViewModel() {

    init {
        BookingProvider.bookingModel = this
    }

    //Subscribe to model, it would observe any changes on data
    var bookingsModel = MutableLiveData<MutableList<Booking>?>()

    fun setBookingList(list: MutableList<Booking>){
        return bookingsModel.postValue(list)
    }

    fun addBooking(booking : Booking){
        var currentList = bookingsModel.value?.toMutableList()
        currentList?.add(booking)
        bookingsModel.postValue(currentList)
    }
    fun testUpdate(){
        var currentList = bookingsModel.value?.toMutableList()
        val newBooking = Booking("Test", "A", "1", "12/03/2022", "20:00", "")
        currentList?.add(newBooking)
        bookingsModel.postValue(currentList)
    }
    fun deleteBooking(booking: Booking){
        var currentList = bookingsModel.value?.toMutableList()
        if (currentList != null) {
            currentList.remove(booking)
            bookingsModel.postValue(currentList)
        }
    }
}