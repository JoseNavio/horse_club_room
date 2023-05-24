package me.josena.hipica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    fun attachFormFragment(){

        supportFragmentManager.commit {
            setReorderingAllowed(true)//Let commit operations decide better operation's order
            addToBackStack(null)
            replace(binding.containerMainActivity.id, FragmentForm.newInstance())
        }
    }

    fun attachListFragment(){

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace(binding.containerMainActivity.id, FragmentList.newInstance())
        }
    }
}