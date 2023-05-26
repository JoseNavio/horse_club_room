package me.josena.hipica.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import me.josena.hipica.R
import me.josena.hipica.databinding.FragmentFormLayoutBinding
import me.josena.hipica.utils.CustomDateValidator
import java.text.SimpleDateFormat
import java.util.*

class FragmentForm : Fragment() {

    private lateinit var binding: FragmentFormLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentFormLayoutBinding.inflate(layoutInflater)
        setButtons()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun setButtons() {

        //DatePicker
        binding.fieldDate.setOnClickListener {

            val builderMaterialDatePicker = MaterialDatePicker.Builder.datePicker()
            val customMaterialSyle = R.style.MaterialCalendarThemeBackground
            builderMaterialDatePicker.setTheme(customMaterialSyle)
                .setTitleText("Selección de proyectos")

            //You can instead set de start and the end of the constraints
            val constraintsBuilder =
                CalendarConstraints.Builder().setValidator(CustomDateValidator())

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
    }

    companion object {
        fun newInstance() = FragmentForm()
    }
}