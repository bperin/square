package com.brianperin.squaredirectory.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Enum class representation of
 * the type of worker related to employee class
 */
@Parcelize
enum class EmployeeType() : Parcelable {
    FULL_TIME, PART_TIME, CONTRACTOR
}