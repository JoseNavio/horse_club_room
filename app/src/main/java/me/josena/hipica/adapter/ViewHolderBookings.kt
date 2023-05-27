package me.josena.hipica.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.josena.hipica.data.Booking
import me.josena.hipica.databinding.ItemBookingBinding

class ViewHolderBookings(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val bindingBooking = ItemBookingBinding.bind(itemView)

    fun render(booking: Booking) {

        bindingBooking.labelRiderName.text = booking.rider
        bindingBooking.labelHorseName.text = booking.horse
        bindingBooking.labelTelephone.text = booking.telephone
        bindingBooking.labelDate.text = booking.date
        bindingBooking.labelTime.text = booking.hour
        bindingBooking.labelComment.text = booking.comment
    }
}