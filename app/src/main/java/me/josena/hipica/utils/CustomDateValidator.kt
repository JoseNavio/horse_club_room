package me.josena.hipica.utils

import android.annotation.SuppressLint
import android.os.Parcel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import me.josena.hipica.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ParcelCreator")
class CustomDateValidator(private val currentYear: Int, private val currentMonth: Int) :
    CalendarConstraints.DateValidator {

    //Calendar.getInstance().apply { set(2023, Calendar.APRIL, 23) }.timeInMillis,

//    private fun isSameDate(date: Long): Boolean {
//
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val formattedDate = dateFormat.format(Date(date))
//
//        for (validDate: Long in markedDates) {
//
//            val formattedValid = dateFormat.format(Date(validDate))
//            if (formattedDate == formattedValid)
//                return true
//        }
//        return false
//    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun isValid(date: Long): Boolean {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(currentYear, currentMonth, currentDay)

        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = date

        return (selectedCalendar.after(currentCalendar) && selectedCalendar.get(Calendar.YEAR) == currentYear) ||
                selectedCalendar.get(Calendar.YEAR) > currentYear
    }
}