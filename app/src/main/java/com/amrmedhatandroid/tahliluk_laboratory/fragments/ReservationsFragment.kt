package com.amrmedhatandroid.tahliluk_laboratory.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.adapters.ReservationsAdapter
import com.amrmedhatandroid.tahliluk_laboratory.databinding.DialogProgressBinding
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentReservationsBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.ReservationListener
import com.amrmedhatandroid.tahliluk_laboratory.models.Reserve
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.ReservationsViewModel
import kotlinx.coroutines.flow.collect


class ReservationsFragment : Fragment(),ReservationListener {
    private lateinit var mReservationBinding: FragmentReservationsBinding
    private lateinit var mReservationsViewModel: ReservationsViewModel
    private lateinit var mReservationsList: ArrayList<Reserve>
    private lateinit var mReservationsListAdapter: ReservationsAdapter
    private lateinit var bindingDialog: DialogProgressBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            mReservationsViewModel = ViewModelProvider(this)[ReservationsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mReservationBinding = FragmentReservationsBinding.inflate(layoutInflater)
        bindingDialog = DialogProgressBinding.inflate(layoutInflater)
         getReservations()

        return mReservationBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ReservationsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun getReservations(){
        lifecycleScope.launchWhenResumed {
            mReservationsViewModel.getReservations(requireContext()).collect {
                if (it.size > 0) {
                    mReservationsList = ArrayList()
                    mReservationsList = it
                    mReservationsList.sortWith { obj1: Reserve, obj2: Reserve ->
                        obj2.orderDateTime!!.compareTo(obj1.orderDateTime!!)
                    }
                    mReservationBinding.rvReservations.visibility = View.VISIBLE

                    mReservationsListAdapter = ReservationsAdapter(mReservationsList,
                        this@ReservationsFragment)
                    mReservationBinding.rvReservations.adapter =
                        mReservationsListAdapter
                    SupportClass.loading(false,
                        null,
                        mReservationBinding.progressBar)
                }
            }
        }

    }

    override fun onReservationClickListener(reserve: Reserve) {

        val reservationDetailsFragment = ReservationDetailsFragment.newInstance()
        val bundle = Bundle()
        bundle.putParcelable(Constants.Reservation, reserve)
        reservationDetailsFragment.arguments = bundle
        val fragmentManager: FragmentManager =
            (mReservationBinding.root.context as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fui_slide_in_right,
            R.anim.fragmentanimation,
            R.anim.fui_slide_in_right,
            R.anim.fragmentanimation
        )
        fragmentTransaction.replace(R.id.fragment_container, reservationDetailsFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }
}