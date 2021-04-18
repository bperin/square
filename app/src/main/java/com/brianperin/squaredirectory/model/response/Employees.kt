package com.brianperin.squaredirectory.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Employees(
    @SerializedName("employees")
    val employees: List<Employee>
) : Parcelable