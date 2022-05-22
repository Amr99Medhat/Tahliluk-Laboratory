package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityMainBinding
import com.amrmedhatandroid.tahliluk_laboratory.fragments.HomeFragment
import com.amrmedhatandroid.tahliluk_laboratory.fragments.ProfileFragment
import com.amrmedhatandroid.tahliluk_laboratory.fragments.SettingsFragment
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.MainViewModel
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mActivityMainBinding: ActivityMainBinding
    private lateinit var mMainActivityViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)
        mMainActivityViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setScreenElements()
        lifecycleScope.launchWhenResumed {
            getToken()
        }
        //TODO("setDeviceLanguage")
        //setDeviceLanguage()
        setListeners()
        //set home fragment initially selected
        mActivityMainBinding.bottomNavigation.show(2, false)
    }

    private fun setListeners() {
        mActivityMainBinding.bottomNavigation.setOnShowListener {
            var fragment: Fragment? = null
            when (it.id) {
                1 -> {
                    fragment = SettingsFragment()
                }
                2 -> {
                    fragment = HomeFragment()
                }
                3 -> {
                    fragment = ProfileFragment()
                }
            }
            loadFragment(fragment)
        }
        mActivityMainBinding.bottomNavigation.setOnClickMenuListener {
            //SupportClass.showToast(this,"click ${it.id}")
        }
        mActivityMainBinding.bottomNavigation.setOnReselectListener {
            return@setOnReselectListener
        }
    }

    private fun setScreenElements() {
        mActivityMainBinding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                1,
                R.drawable.ic_settings_bottom_nav
            )
        )
        mActivityMainBinding.bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_home))
        mActivityMainBinding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                3,
                R.drawable.ic_profile
            )
        )
    }

    private suspend fun getToken() {
        mMainActivityViewModel.getToken().collect {
            if (it != Constants.KEY_EMPTY) {
                updateToken(applicationContext, it)
            }

        }
    }

    private suspend fun updateToken(context: Context, token: String) {
        mMainActivityViewModel.updateToken(context, token).collect {
            when (it) {
                Constants.KEY_FALSE_RETURN -> {
                    SupportClass.showToast(
                        applicationContext,
                        resources.getString(R.string.unable_to_updated_token)
                    )
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment?) {
        //Replace fragments
        supportFragmentManager
            .beginTransaction()
            .replace(mActivityMainBinding.frameLayout.id, fragment!!)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mMainActivityViewModel.viewModelScope.cancel()
    }

}