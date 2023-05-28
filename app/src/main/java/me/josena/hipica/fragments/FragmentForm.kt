package me.josena.hipica.fragments

import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.josena.hipica.R
import me.josena.hipica.data.*
import me.josena.hipica.databinding.FragmentFormLayoutBinding
import me.josena.hipica.utils.CustomDateValidator
import java.text.SimpleDateFormat
import java.util.*

class FragmentForm(private val app: BookingApp) : Fragment() {

    private lateinit var binding: FragmentFormLayoutBinding
    private lateinit var bookingDB: SQLiteBookingDB
    private lateinit var bookingDAO: BookingDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentFormLayoutBinding.inflate(layoutInflater)

        //todo Test mock
        binding.fieldRider.setOnLongClickListener{
            binding.fieldRider.setText("Pablo Motos")
            binding.fieldHorse.setText("Macarena")
            binding.fieldTelephone.setText("666666666")
            binding.fieldDate.setText("12/07/2023")
            binding.fieldTime.setText("18:00")
            binding.fieldComment.setText("Si")
            true
        }

        //DB
        bookingDB = app.room.build()
        bookingDAO = bookingDB?.bookingDAO()!!
        setButtons()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun setButtons() {

        //Confirm
        binding.buttonConfirm.setOnClickListener {
            confirmBooking()
        }
        //DatePicker
        binding.fieldDate.setOnClickListener {

            val builderMaterialDatePicker = MaterialDatePicker.Builder.datePicker()
            val customMaterialSyle = R.style.MaterialCalendarThemeBackground
            builderMaterialDatePicker.setTheme(customMaterialSyle)
                .setTitleText("Selección de proyectos")

            //Get current date
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)

            //You can instead set de start and the end of the constraints
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(CustomDateValidator(currentYear, currentMonth))

            builderMaterialDatePicker.setCalendarConstraints(constraintsBuilder.build())
            val materialDatePicker = builderMaterialDatePicker.build()

            //When clicking a date
            materialDatePicker.addOnPositiveButtonClickListener { selection ->

                val calendar = Calendar.getInstance()
                //Transform selected date to a calendar object
                calendar.timeInMillis = selection
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormatter.format(calendar.time)
                binding.fieldDate.setText(formattedDate)
            }
            childFragmentManager.let { it -> materialDatePicker.show(it, "CustomDatePicker") }
        }
        //Time clock
        binding.fieldTime.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = 0

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                if (hourOfDay in 17..20 && minute == 0) {
                    val selectedTime = String.format("%02d:00", hourOfDay)
                    binding.fieldTime.setText(selectedTime)
                } else {
                    // Here, we simply show a toast message
                    Toast.makeText(
                        context,
                        "Selecciona una hora entre las 17 y las 20",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun confirmBooking() {
        val rider = binding.fieldRider.text.toString().trim()
        val horse = binding.fieldHorse.text.toString().trim()
        val telephone = binding.fieldTelephone.text.toString().trim()
        val date = binding.fieldDate.text.toString().trim()
        val hour = binding.fieldTime.text.toString().trim()
        val comment = binding.fieldComment.text.toString().trim()

        if (rider.isNotEmpty() && horse.isNotEmpty() && telephone.isNotEmpty() && date.isNotEmpty() && hour.isNotEmpty()) {
            val newBooking = Booking(rider, horse, telephone, date, hour, comment)
            //Insert using Room
            lifecycleScope.launch(Dispatchers.IO) {
                bookingDAO.insertBooking(newBooking)
                BookingProvider.bookingModel?.addBooking(newBooking)
            }
            //Notify
            sendWhatsApp(newBooking.toString())
            resetFields()

        } else {
            // Display an error message or handle the case when any field is blank
            Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    //WhatsApp
    //It is necessary to add the package query on the manifest in order to use this...
    private fun isWhatsAppInstalled(): Boolean {
        val whatsAppPackageName = "com.whatsapp"
        return try {
            val packageInfo = context?.packageManager?.getPackageInfoCompat(
                whatsAppPackageName,
                PackageManager.GET_ACTIVITIES
            )
            packageInfo != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun PackageManager.getPackageInfoCompat(
        packageName: String,
        flags: Int
    ): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }

    private fun sendWhatsApp(message: String) {

        isWhatsAppInstalled()

        if (isWhatsAppInstalled()) {
            val number = binding.fieldTelephone?.text.toString()
            if (message.isNotBlank() && number.isNotBlank()) {

                val whatsIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=34$number&text=$message")
                )
                startActivity(whatsIntent)
            } else {
                Toast.makeText(
                    context,
                    "Los campos no pueden estar vacios.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            Toast.makeText(
                context,
                "No existe la aplicación de WhatsApp en el dispositivo para mandar la notificación.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun resetFields(){
        binding.fieldRider.setText("")
        binding.fieldHorse.setText("")
        binding.fieldTelephone.setText("")
        binding.fieldDate.setText("")
        binding.fieldTime.setText("")
        binding.fieldComment.setText("")
    }
    companion object {
        fun newInstance(app: BookingApp) = FragmentForm(app)
    }
}