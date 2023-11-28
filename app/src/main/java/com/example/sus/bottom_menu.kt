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
        binding = ActivityBottomMenuBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main2)
        replaceFragment(general1())

        val intent = intent
        val activityName = intent.getStringExtra("activityName")
        Log.d("check_bottom_menu", activityName.toString())
        if (activityName == "general_activity" ){
            setContentView(R.layout.activity_main2)
            replaceFragment(general1())}

        if (activityName == "profile_activity") {
            setContentView(R.layout.activity_main3)
            replaceFragment(profile1())}

        setContentView(binding.root)

        SharedPrefManager.refreshDataUsingRefreshToken()
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.profileFragment -> replaceFragment(profile1())
                R.id.homeFragment -> replaceFragment(general1())
                R.id.etcFragment -> replaceFragment(EtcMenu())
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