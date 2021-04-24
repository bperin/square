package com.brianperin.squaredirectory.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.brianperin.squaredirectory.R
import com.brianperin.squaredirectory.fragment.EmployeesFragment
import com.brianperin.squaredirectory.util.Constants
import com.brianperin.squaredirectory.viewmodel.EmployeesViewModel
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val employeesViewModel: EmployeesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            showEmployeesFragment()
        }

        //call our get employees networking request, ur activity doesn't use the result
        //but the fragment will have the data quicker
        employeesViewModel.getEmployees(EmployeesViewModel.SortMethod.NAME)

    }

    /**
     * Init our main fragment with needed permission todo handle non permission access
     */
    private fun showEmployeesFragment() = runWithPermissions(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    ) {

        val employeesFragment = EmployeesFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, employeesFragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(Constants.TIMBER).d("main activity destroyed")
    }
}