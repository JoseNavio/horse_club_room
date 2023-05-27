package me.josena.hipica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.josena.hipica.R
import me.josena.hipica.data.Booking

class AdapterBookings(private val bookings: MutableList<Booking>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)

        return ViewHolderBookings(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderBookings)
            holder.render(bookings[position])
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

}