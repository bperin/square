package com.brianperin.squaredirectory.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.brianperin.squaredirectory.R
import com.brianperin.squaredirectory.adapter.EmployeesAdapter
import com.brianperin.squaredirectory.viewmodel.EmployeesViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Fragment responsible for syncing our views
 * to view model / repository / domain for fetching employees
 * and updating the adapter used to display list of employees
 */
class EmployeesFragment() : BaseFragment() {

    private val employeesViewModel: EmployeesViewModel by activityViewModels()
    private lateinit var employeesAdapter : EmployeesAdapter

    companion object {
        fun newInstance() = EmployeesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        employeesAdapter = EmployeesAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_employees, container, false)
    }

    /**
     * Fetch our employees once view exists
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}