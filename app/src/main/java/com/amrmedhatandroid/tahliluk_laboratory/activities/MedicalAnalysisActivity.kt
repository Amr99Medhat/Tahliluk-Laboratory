package com.amrmedhatandroid.tahliluk_laboratory.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityMedicalAnalysisBinding
import com.amrmedhatandroid.tahliluk_laboratory.fragments.MedicalAnalyticsFragment

class MedicalAnalysisActivity : AppCompatActivity() {
    private lateinit var mMedicalAnalysisBinding: ActivityMedicalAnalysisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMedicalAnalysisBinding = ActivityMedicalAnalysisBinding.inflate(layoutInflater)
        setContentView(mMedicalAnalysisBinding.root)
        replaceFragment(MedicalAnalyticsFragment.newInstance())
        setListeners()


    }


    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fui_slide_in_right,
            R.anim.fragmentanimation,
            R.anim.fui_slide_in_right,
            R.anim.fragmentanimation)
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.isAddToBackStackAllowed
        fragmentTransaction.commit()
    }

    private fun setListeners(){
        mMedicalAnalysisBinding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }
}