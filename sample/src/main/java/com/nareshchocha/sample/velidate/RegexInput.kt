package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput


class RegexInput(
    private val regex: String,
    private val skipEmpty: Boolean = false,
    value: String = "",
    isPure: Boolean = true
) : FormzInput<String, ValidationError>(value, isPure) {
    override fun validator(value: String): ValidationError? {
        return if (value.isEmpty()) {
            if (skipEmpty) null else ValidationError.EMPTY
        } else if (!Regex(regex).matches(value)) {
            ValidationError.INVALID
        } else {
            null
        }
    }

    fun copy(value: String, isPure: Boolean = false): RegexInput {
        return RegexInput(regex, skipEmpty, value, isPure = isPure)
    }
}


object RegularExpressions {
    const val USERNAME = "^[A-Za-z0-9_.-]{3,15}$";
    const val EMAIL = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    const val MOBILE_NUMBER = "^\\  d{10}$";
    const val PASSWORD =
        "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    const val URL =
        "((https?:www\\.)|(https?:\\/\\/)|(www\\.))[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9]{1,6}(\\/[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)?";
}