package com.brianperin.squaredirectory.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * High level from our main http response
 * added some helipng methods for sorthing
 */
@Parcelize
data class Employees(
    @SerializedName("employees")
    var employees: List<Employee> = emptyList<Employee>()
) : Parcelable {
    fun sortByName() {
        employees = employees.sortedBy { it.fullName }
    }

    fun sortByTeam() {
        employees = employees.sortedBy { it.team }
    }

    fun sortByType(){
        employees = employees.sortedBy { it.employeeType }
    }
}