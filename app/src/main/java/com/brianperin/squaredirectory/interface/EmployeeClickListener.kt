package com.brianperin.squaredirectory.`interface`

import android.view.View
import com.brianperin.squaredirectory.model.response.Employee

interface EmployeeClickListener {
    fun onClick(store: Employee?, view: Int?, position: View?)
}