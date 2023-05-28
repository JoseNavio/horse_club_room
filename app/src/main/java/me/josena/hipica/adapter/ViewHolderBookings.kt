package me.josena.hipica.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.josena.hipica.R
import me.josena.hipica.data.Booking
import me.josena.hipica.databinding.ItemBookingBinding

class ViewHolderBookings(itemView: View, adapterBookings: AdapterBookings) : RecyclerView.ViewHolder(itemView) {

    private val bindingBooking = ItemBookingBinding.bind(itemView)

    init{

        itemView.setOnClickListener{
            adapterBookings.currentSelected(absoluteAdapterPosition)
        }
    }
    fun render(booking: Booking) {
        bindingBooking.cardView.setCardBackgroundColor(Color.WHITE)
        bindingBooking.labelRiderName.text = booking.rider
        bindingBooking.labelHorseName.text = booking.horse
        bindingBooking.labelTelephone.text = booking.telephone
        bindingBooking.labelDate.text = booking.date
        bindingBooking.labelTime.text = booking.hour
        bindingBooking.labelComment.text = booking.comment
    }
    fun renderSelected(booking: Booking){

        bindingBooking.cardView.setCardBackgroundColor(Color.GRAY)
        bindingBooking.labelRiderName.text = booking.rider
        bindingBooking.labelHorseName.text = booking.horse
        bindingBooking.labelTelephone.text = booking.telephone
        bindingBooking.labelDate.text = booking.date
        bindingBooking.labelTime.text = booking.hour
        bindingBooking.labelComment.text = booking.comment
    }
}