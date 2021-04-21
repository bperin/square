package com.brianperin.squaredirectory.`interface`

import android.view.View
import com.brianperin.squaredirectory.model.response.Employee

interface EmployeeClickListener {
    fun onClick(employee: Employee, view: Int, position: View)
}