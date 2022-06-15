package com.amrmedhatandroid.tahliluk_laboratory.fragments
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.adapters.MedicalAnalyticsAdapter
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentMedicalAnalyticsBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.MedicalAnalysisViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


class MedicalAnalyticsFragment : Fragment() {
    private lateinit var mFragmentMedicalAnalyticsBinding: FragmentMedicalAnalyticsBinding
    private lateinit var mMedicalAnalyticsViewModel: MedicalAnalysisViewModel
    private lateinit var mMedicalAnalyticsList: ArrayList<Analytics>
    private lateinit var mMedicalAnalyticsAdapter: MedicalAnalyticsAdapter
    private var parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMedicalAnalyticsViewModel = ViewModelProvider(this)[MedicalAnalysisViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentMedicalAnalyticsBinding = FragmentMedicalAnalyticsBinding.inflate(layoutInflater)
        getMedicalAnalytics()
        setListeners()
        swipeHolder()
        return mFragmentMedicalAnalyticsBinding.root
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            MedicalAnalyticsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }



    private fun getMedicalAnalytics() {
        val preference = PreferenceManager(requireContext())
        val labId = preference.getString(Constants.KEY_LAB_ID)
        lifecycleScope.launchWhenResumed {
            mMedicalAnalyticsViewModel.getMedicalAnalysis(
                labId!!,
                Constants.KEY_COLLECTION_LABS
            ).collect {
                        mMedicalAnalyticsList = it
                    if (mMedicalAnalyticsList.size > 0) {
                        mFragmentMedicalAnalyticsBinding.rvMedicalAnalysis.visibility = View.VISIBLE
                        mFragmentMedicalAnalyticsBinding.tvNoAnalytics.visibility=View.GONE
                        mMedicalAnalyticsAdapter = MedicalAnalyticsAdapter(mMedicalAnalyticsList)
                        mFragmentMedicalAnalyticsBinding.rvMedicalAnalysis.layoutManager =
                            LinearLayoutManager(requireContext())
                        mFragmentMedicalAnalyticsBinding.rvMedicalAnalysis.adapter =
                            mMedicalAnalyticsAdapter
                        SupportClass.loading(false,
                            null,
                            mFragmentMedicalAnalyticsBinding.progressBar)

                    }
                coroutineScope.launch {
                    delay(1000)
                    if(mMedicalAnalyticsList.isEmpty())
                    {
                        mFragmentMedicalAnalyticsBinding.tvNoAnalytics.visibility=View.VISIBLE
                        SupportClass.loading(false,
                            null,
                            mFragmentMedicalAnalyticsBinding.progressBar)
                    }
                }
                }
                }
        }


    private fun setListeners(){
        mFragmentMedicalAnalyticsBinding.btnAddAnalysis.setOnClickListener {
            val addMedicalAnalysisFragment = AddMedicalAnalysisFragment.newInstance()
            val fragmentManager: FragmentManager =
                (mFragmentMedicalAnalyticsBinding.root.context as FragmentActivity).supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.fui_slide_in_right,
                R.anim.fragmentanimation,
                R.anim.fui_slide_in_right,
                R.anim.fragmentanimation
            )
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.fragment_container, addMedicalAnalysisFragment)
            fragmentTransaction.commit()
        }
    }


    private fun swipeHolder() {
        val simpleCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id: Int = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.RIGHT) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.dialog_delete_analysis_title))
                    builder.setMessage(getString(R.string.confirm_delete_message))
                    builder.setCancelable(false)
                    builder.setPositiveButton(
                        resources.getText(R.string.btn_yes)
                    ) { dialog, which ->
                        lifecycleScope.launchWhenResumed {
                            mMedicalAnalyticsList.removeAt(id)
                            mMedicalAnalyticsAdapter.setNewList(mMedicalAnalyticsList)
                            mMedicalAnalyticsViewModel.deleteMedicalAnalysis(
                                requireContext(),
                                Constants.KEY_COLLECTION_LABS,
                                mMedicalAnalyticsList
                            )
                            if (mMedicalAnalyticsList.isEmpty()){

                                mFragmentMedicalAnalyticsBinding.tvNoAnalytics.visibility=View.VISIBLE
                            }
                        }
                    }
                    builder.setNegativeButton(
                        resources.getText(R.string.btn_no)
                    ) { dialog, which ->
                        mMedicalAnalyticsAdapter.notifyItemChanged(id)
                    }
                    val dialog = builder.create()
                    dialog.show()

                } else {
                    var analysis = mMedicalAnalyticsList[id]
                    val editMedicalAnalysisFragment = EditMedicalAnalysisFragment.newInstance()
                    var bundle =Bundle()
                    bundle.putSerializable(Constants.EDIE_ANALYSIS,analysis)
                    bundle.putInt(Constants.ANALYSIS_POSITION,id)
                    editMedicalAnalysisFragment.arguments = bundle
                    val fragmentManager: FragmentManager =
                        (mFragmentMedicalAnalyticsBinding.root.context as FragmentActivity).supportFragmentManager
                    val fragmentTransaction: FragmentTransaction =
                        fragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(
                        R.anim.fui_slide_in_right,
                        R.anim.fragmentanimation,
                        R.anim.fui_slide_in_right,
                        R.anim.fragmentanimation
                    )
                    fragmentTransaction.replace(R.id.fragment_container, editMedicalAnalysisFragment)
                        .addToBackStack(null)
                    fragmentTransaction.commit()
                }

        }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                 RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                     .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(),R.color.primary_dark))
                     .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit_24)
                     .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(),R.color.delete_background))
                     .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                     .create()
                     .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        ItemTouchHelper(simpleCallback).attachToRecyclerView(mFragmentMedicalAnalyticsBinding.rvMedicalAnalysis)




    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mMedicalAnalyticsViewModel.viewModelScope.cancel()
    }
}
