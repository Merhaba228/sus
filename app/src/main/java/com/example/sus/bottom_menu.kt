package com.example.sus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sus.databinding.ActivityBottomMenuBinding
import android.util.Log

class bottom_menu : AppCompatActivity() {

    private lateinit var binding : ActivityBottomMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("bottom_menu", "check_1")

        binding = ActivityBottomMenuBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main2)
        setContentView(binding.root)
        replaceFragment(general1())
        SharedPrefManager.refreshDataUsingRefreshToken()
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.profileFragment -> replaceFragment(profile1())
                R.id.homeFragment -> replaceFragment(general1())
                else ->{


                }

            }

            true

        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}