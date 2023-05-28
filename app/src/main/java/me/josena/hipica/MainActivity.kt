package me.josena.hipica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import androidx.fragment.app.commit
import me.josena.hipica.data.BookingApp
import me.josena.hipica.databinding.ActivityMainBinding
import me.josena.hipica.fragments.FragmentForm
import me.josena.hipica.fragments.FragmentList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var app: BookingApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //todo Maybe is enough to call context.applicationContext on fragments
        app = applicationContext as BookingApp

        binding.imageView.setImageResource(R.drawable.horse)
        setButtons()
    }

    private fun setButtons() {
        binding.buttonLaunchForm.setOnClickListener {
            binding.imageView.visibility = GONE
            attachFormFragment()
            binding.imageView.isEnabled = false
            binding.buttonLaunchForm.isEnabled = false
            binding.buttonLaunchList.isEnabled = true
        }
        binding.buttonLaunchList.setOnClickListener {
            binding.imageView.visibility = GONE
            attachListFragment()
            binding.buttonLaunchForm.isEnabled = true
            binding.buttonLaunchList.isEnabled = false
        }
    }

    private fun attachFormFragment() {

        supportFragmentManager.commit {
            setReorderingAllowed(true)//Let commit operations decide better operation's order
            replace(binding.containerMainActivity.id, FragmentForm.newInstance(app))
        }
    }

    private fun attachListFragment() {

        supportFragmentManager.commit {

            setReorderingAllowed(true)
            replace(binding.containerMainActivity.id, FragmentList.newInstance(app))
        }
    }
}