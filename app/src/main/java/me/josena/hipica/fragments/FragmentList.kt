package me.josena.hipica.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.josena.hipica.adapter.AdapterBookings
import me.josena.hipica.adapter.OnItemSelected
import me.josena.hipica.data.Booking
import me.josena.hipica.data.BookingApp
import me.josena.hipica.data.SQLiteBookingDB
import me.josena.hipica.databinding.DialogModifyBinding
import me.josena.hipica.databinding.DialogSearchBinding
import me.josena.hipica.databinding.FragmentListLayoutBinding
import me.josena.hipica.viewmodel.BookingViewModel
import java.util.*

class FragmentList(private val app: BookingApp) : Fragment() {

    private lateinit var binding: FragmentListLayoutBinding
    private lateinit var recycler: RecyclerView
    private lateinit var adapterBookings: AdapterBookings
    private lateinit var bookings: MutableList<Booking>
    private lateinit var bookingDB: SQLiteBookingDB

    private lateinit var bookingViewModel: BookingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListLayoutBinding.inflate(layoutInflater)
        bookingDB = app.room.build()

        initAdapter()
        setButtons()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    private fun initAdapter() {
        lifecycleScope.launch(Dispatchers.IO) {
            bookings = bookingDB.bookingDAO().getAllBookings()
            withContext(Dispatchers.Main) {
                recycler = binding.recyclerBookings
                adapterBookings = AdapterBookings(bookings)
                recycler.adapter = adapterBookings
                recycler.layoutManager = LinearLayoutManager(context)
                initObserver()
            }
        }
    }

    private fun initObserver() {

        // Initialize the ViewModel
        bookingViewModel = ViewModelProvider(this)[BookingViewModel::class.java]
        //Pass the list to the view model
        bookingViewModel.setBookingList(bookings)

        bookingViewModel.bookingsModel.observe(this, Observer { updatedBookings ->
            updatedBookings?.let {
                bookings = it
                adapterBookings.updateBookings(bookings)
            }
        })
    }

    private fun setButtons() {

        binding.buttonSearch.setOnClickListener {
            showSearchDialog()
        }
        binding.buttonModify.setOnClickListener {

            adapterBookings.selectedBooking(object : OnItemSelected {

                override fun onItemSelected(position: Int) {

                    if (position != RecyclerView.NO_POSITION) {
                        showModifyDialog(position)
                    }
                }
            })
        }
        binding.buttonDelete.setOnClickListener {

            adapterBookings.selectedBooking(object : OnItemSelected {

                override fun onItemSelected(position: Int) {

                    if (position != RecyclerView.NO_POSITION) {
                        adapterCallDelete(position)
                    }
                }
            })
        }
    }

    private fun adapterCallDelete(position: Int) {
        bookingViewModel.deleteBooking(bookings[position])
        lifecycleScope.launch(Dispatchers.IO) {
            bookingDB.bookingDAO().deleteBooking(bookings[position])
        }
    }

    private fun fiterByDateAndHour(date: String, hour: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            val filteredList = bookingDB.bookingDAO().getBookingsByDateAndHour(date, hour)
            withContext(Dispatchers.Main) {
                adapterBookings.updateBookings(filteredList)
            }
        }
    }

    private fun modifyBooking(rider: String, horse: String, telephone: String, comment: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            bookingDB.bookingDAO().updateBookingDetails(rider, horse, telephone, comment)
            bookings = bookingDB.bookingDAO().getAllBookings()
            withContext(Dispatchers.Main) {
                adapterBookings.updateBookings(bookings)
            }
        }
    }

    private fun showModifyDialog(position: Int) {

        val selectedBooking = bookings[position];
        val rider = selectedBooking.rider

        Toast.makeText(context, "$rider modifique los campos erróneos.", Toast.LENGTH_SHORT).show()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Modificar reserva")

        val bindingDialog = DialogModifyBinding.inflate(layoutInflater)
        builder.setView(bindingDialog.root)

        bindingDialog.dialogHorse.setText(selectedBooking.horse)
        bindingDialog.dialogTelephone.setText(selectedBooking.telephone)
        bindingDialog.dialogComment.setText(selectedBooking.comment)

        builder.setPositiveButton("Aceptar") { _, _ ->

            val finalHorse = bindingDialog.dialogHorse.text.toString()
            val finalTelephone = bindingDialog.dialogTelephone.text.toString()
            val finalComment = bindingDialog.dialogComment.text.toString()

            //Modify the entry on the DB and update the info on the item
            modifyBooking(rider, finalHorse, finalTelephone, finalComment)
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        builder.create().show()
    }

    private fun showSearchDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Fecha y hora")

        val bindingDialog = DialogSearchBinding.inflate(layoutInflater)
        builder.setView(bindingDialog.root)

        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val hour = currentDate.get(Calendar.HOUR_OF_DAY)
        val minute = currentDate.get(Calendar.MINUTE)

        bindingDialog.dialogDate.setOnClickListener {
            context?.let<Context, DatePickerDialog> { it ->
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        bindingDialog.dialogDate.setText(
                            String.format(
                                "%02d/%02d/%04d",
                                dayOfMonth,
                                monthOfYear + 1,
                                year
                            )
                        )
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }

        bindingDialog.dialogHour.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, _ ->
                    bindingDialog.dialogHour.setText(String.format("%02d:%02d", hourOfDay, 0))
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        builder.setPositiveButton("Buscar") { _, _ ->
            val selectedDate = bindingDialog.dialogDate.text.toString()
            val selectedTime = bindingDialog.dialogHour.text.toString()

            fiterByDateAndHour(selectedDate, selectedTime)
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        builder.create().show()
    }

    companion object {
        fun newInstance(app: BookingApp) = FragmentList(app)
    }
}