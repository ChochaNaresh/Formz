package com.nareshchocha.sample.velidate

import com.nareshchocha.formz.FormzInput

class BooleanInput(
    value: Boolean = false,
    isPure: Boolean = true
) : FormzInput<Boolean, ValidationError>(value, isPure) {
    override fun validator(value: Boolean): ValidationError? {
        return if (value) null else ValidationError.NOT_SELECTED
    }

    fun copy(value: Boolean, isPure: Boolean = false): BooleanInput {
        return BooleanInput(value, isPure = isPure)
    }
}
