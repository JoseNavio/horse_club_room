package me.josena.hipica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import me.josena.hipica.databinding.ActivityMainBinding
import me.josena.hipica.fragments.FragmentForm
import me.josena.hipica.fragments.FragmentList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtons()
    }
    private fun setButtons(){
        binding.buttonLaunchForm.setOnClickListener{
            attachFormFragment()
        }
        binding.buttonLaunchList.setOnClickListener{
            attachListFragment()
        }
    }
    private fun attachFormFragment(){

        supportFragmentManager.commit {
            setReorderingAllowed(true)//Let commit operations decide better operation's order
            replace(binding.containerMainActivity.id, FragmentForm.newInstance())
            addToBackStack(null)
        }
    }

    private fun attachListFragment(){

        supportFragmentManager.commit {

            setReorderingAllowed(true)
            replace(binding.containerMainActivity.id, FragmentList.newInstance())
            addToBackStack(null)
        }
    }
}