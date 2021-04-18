package com.brianperin.squaredirectory.model.response

import android.os.Build
import android.os.Parcelable
import android.telephony.PhoneNumberUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Data object representing a single employee
 */
@Parcelize
data class Employee(

    @SerializedName("uuid")
    val id: UUID,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("phone_number")
    val phoneNumber: String?,

    @SerializedName("email_address")
    val emailAddress: String,

    @SerializedName("biography")
    val biography: String?,

    @SerializedName("photo_url_small")
    val photoSmall: String?,

    @SerializedName("photo_url_large")
    val photoLarge: String?,

    @SerializedName("team")
    val team: String,

    @SerializedName("employee_type")
    val employeeType: EmployeeType,

    ) : Parcelable {

    /**
     * Format a number to be human readable
     */
    val formattedNumber: String?
        get() {
            this.phoneNumber?.let {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    PhoneNumberUtils.formatNumber(it, Locale.getDefault().country)
                } else {
                    PhoneNumberUtils.formatNumber(it)
                }
            }
            return null
        }
}