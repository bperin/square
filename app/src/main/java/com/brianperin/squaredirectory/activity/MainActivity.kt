package com.brianperin.squaredirectory.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.brianperin.squaredirectory.R
import com.brianperin.squaredirectory.fragment.EmployeesFragment
import com.brianperin.squaredirectory.viewmodel.EmployeesViewModel
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

class MainActivity : AppCompatActivity() {

    val employeesViewModel: EmployeesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showEmployeesFragment()
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
}