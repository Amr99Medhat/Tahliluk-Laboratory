package com.amrmedhatandroid.tahliluk_laboratory.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityReservationsBinding
import com.amrmedhatandroid.tahliluk_laboratory.fragments.ReservationsFragment

class ReservationsActivity : AppCompatActivity() {
    lateinit var binding: ActivityReservationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setListeners()
        replaceFragment(ReservationsFragment.newInstance())
    }


    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.fui_slide_in_right,R.anim.fragmentanimation,R.anim.fui_slide_in_right,R.anim.fragmentanimation)
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.isAddToBackStackAllowed
        fragmentTransaction.commit()
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }
}