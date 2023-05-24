package me.josena.hipica.fragments

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.josena.hipica.databinding.FragmentWhatsappLayoutBinding

class FragmentWhatsApp : Fragment() {

    private lateinit var binding: FragmentWhatsappLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentWhatsappLayoutBinding.inflate(layoutInflater)


        binding.buttonSend?.let { button ->
            button.setOnClickListener {

                isWhatsAppInstalled()

                val code = binding.countryCodePicker?.selectedCountryCode

                if (isWhatsAppInstalled()) {
                    val number = code + binding.fieldNumber?.text.toString()
                    val message = binding.fieldMessage?.text.toString()
                    if (message.isNotBlank() && number.isNotBlank()) {

                        val whatsIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone=$number&text=$message")
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
                        "No existe la aplicación de WhatsApp en el dispositivo.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

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
            Log.d("Navio_PM", "Tiramisu")
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            Log.d("Navio_PM", "Older")
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }
}