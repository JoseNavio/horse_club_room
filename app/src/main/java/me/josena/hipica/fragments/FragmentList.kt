package me.josena.hipica.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch
import me.josena.hipica.adapter.AdapterBookings
import me.josena.hipica.data.Booking
import me.josena.hipica.data.SQLiteBookingDB
import me.josena.hipica.databinding.FragmentListLayoutBinding
import me.josena.hipica.viewmodel.BookingViewModel

class FragmentList : Fragment() {

    private lateinit var binding: FragmentListLayoutBinding

    private lateinit var recycler: RecyclerView
    private lateinit var adapterBookings: AdapterBookings

    private lateinit var bookings: MutableList<Booking>



    private val bookingViewModel: BookingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo Room
        val dataBase = context?.let {
            Room.databaseBuilder(it, SQLiteBookingDB::class.java, "SQLite_BookingDB").build()
        }
        val bookingMock = Booking("Juan Garrido", "Cipote", "666112233", "29/05/2023", "19:00", "")

        lifecycleScope.launch {
            dataBase?.bookingDAO()?.insertBooking(bookingMock) ?: Log.d(
                "Navio_Room",
                "Not inserted"
            )
            val bookings = dataBase?.bookingDAO()?.getAllBookings()
        }
        initObserver()

        //todo Test
        bookings = mutableListOf<Booking>(
            Booking("Juan Garrido", "Cipote", "666112233", "29/05/2023", "19:00", "La Madre del cordero."),
            Booking("Lucia Pomodoro", "Reina", "666112234", "30/05/2023", "18:00", "")
        )

        binding = FragmentListLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Prepare recycler
        recycler = binding.recyclerBookings
        adapterBookings = AdapterBookings(bookings)
        recycler.adapter = adapterBookings
        //Recycler layout
        recycler.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun initObserver() {
        bookingViewModel.bookingsModel.observe(this, Observer { updatedProjects ->

            bookings = updatedProjects
            adapterBookings = AdapterBookings(bookings)
            recycler.adapter = adapterBookings
        })
//        bookings= bookingViewModel.getAllBookings()
    }

    companion object {
        fun newInstance() = FragmentList()
    }
}