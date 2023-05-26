package me.josena.hipica.utils

import android.annotation.SuppressLint
import android.os.Parcel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import me.josena.hipica.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ParcelCreator")
class CustomDateValidator :  CalendarConstraints.DateValidator {

    //todo Just for testing
    val markedDates = arrayOf(
        Calendar.getInstance().apply { set(2023, Calendar.APRIL, 23) }.timeInMillis,
        Calendar.getInstance().apply { set(2023, Calendar.APRIL, 25) }.timeInMillis,
        Calendar.getInstance().apply { set(2023, Calendar.MAY, 2) }.timeInMillis,
        Calendar.getInstance().apply { set(2023, Calendar.MARCH, 5) }.timeInMillis,
        Calendar.getInstance().apply { set(2023, Calendar.MARCH, 18) }.timeInMillis,
        Calendar.getInstance().apply { set(2023, Calendar.APRIL, 27) }.timeInMillis
    )

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun isValid(date: Long): Boolean {
        return isSameDate(date)
    }

    private fun isSameDate(date: Long): Boolean {

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(date))

        for (validDate: Long in markedDates) {

            val formattedValid = dateFormat.format(Date(validDate))
            if (formattedDate == formattedValid)
                return true
        }
        return false
    }
}