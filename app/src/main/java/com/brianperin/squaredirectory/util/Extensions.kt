package com.brianperin.squaredirectory.util

import com.brianperin.squaredirectory.model.response.Employee
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotNull

fun Employee.validate() {
    org.valiktor.validate(this) {
        validate(Employee::id).isNotNull()
        validate(Employee::fullName).isNotNull()
        validate(Employee::phoneNumber).hasSize(10)
        validate(Employee::emailAddress).isEmail()
    }
}