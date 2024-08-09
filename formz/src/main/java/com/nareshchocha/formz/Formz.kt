package com.nareshchocha.formz

import com.nareshchocha.formz.utilities.extentions.isNull
import java.util.Objects


/** Enum representing the submission status of a form.*/
enum class FormzSubmissionStatus {
    /** The form has not yet been submitted.*/
    INITIAL,

    /** The form is in the process of being submitted.*/
    IN_PROGRESS,

    /** The form has been submitted successfully.*/
    SUCCESS,

    /** The form submission failed.*/
    FAILURE,

    /** The form submission has been canceled.*/
    CANCELED;

    /** Indicates whether the form has not yet been submitted.*/
    fun isInitial() = this == INITIAL;

    /** Indicates whether the form is in the process of being submitted.*/
    fun isInProgress() = this == IN_PROGRESS;

    /** Indicates whether the form has been submitted successfully.*/
    fun isSuccess() = this == SUCCESS;

    /** Indicates whether the form submission failed.*/
    fun isFailure() = this == FAILURE;

    /** Indicates whether the form submission has been canceled.*/
    fun isCanceled() = this == CANCELED;

    /** Indicates whether the form is either in progress or has been submitted
     * successfully.
     *
     * This is useful for showing a loading indicator or disabling the submit
     * button to prevent duplicate submissions.*/
    fun isInProgressOrSuccess() = isInProgress() || isSuccess()
}

abstract class FormzInput<T, E>(val value: T, val isPure: Boolean) {

    /** Whether the [FormzInput] value is valid according to the
     * overridden `validator`.
     *
     * Returns `true` if `validator` returns `null` for the
     * current [FormzInput] value and `false` otherwise.*/
    fun isValid() = validator(value) == null

    /** Whether the [FormzInput] value is not valid.
     * A value is invalid when the overridden `validator`
     * returns an error (non-null value).*/
    val isNotValid: Boolean
        get() = !displayError().isNull

    /** Returns a validation error if the [FormzInput] is invalid.
     * Returns `null` if the [FormzInput] is valid.*/
    fun error() = validator(value)

    /** The error to display if the [FormzInput] value
     * is not valid and has been modified.*/
    fun displayError() = if (isPure) null else error()

    /** A function that must return a validation error if the provided
     * [value] is invalid and `null` otherwise.*/
    abstract fun validator(value: T): E?


    override fun hashCode(): Int {
        return Objects.hash(value, isPure)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FormzInput<*, *>) return false

        if (value != other.value) return false
        if (isPure != other.isPure) return false

        return true
    }

    override fun toString(): String {
        return "FormzInput(value=$value, isPure=$isPure, isValid=${isValid()}, error=${error()})"
    }
}

/** Class which contains methods that help manipulate and manage
 * validity of [FormzInput] instances.*/
object Formz {
    /** Returns a [bool] given a list of [FormzInput] indicating whether
     * the inputs are all valid.*/
    fun validate(inputs: List<FormzInput<*, *>>): Boolean {
        return inputs.all { it.isValid() }
    }

    /** Returns a [bool] given a list of [FormzInput] indicating whether
     * all the inputs are pure.*/
    fun isPure(inputs: List<FormzInput<*, *>>): Boolean {
        return inputs.all { it.isPure }
    }
}


/** Interface that automatically handles validation of all [FormzInput]s present in
 * the [inputs].
 *
 * When interface this in, you are required to override the [inputs] getter and
 * provide all [FormzInput]s you want to automatically validate.
 *
 * ```kotlin
 * class LoginFormState(
 *   val username: RegexInput = RegexInput(regex = RegularExpressions.userName, isPure = true),
 *    val password: RegexInput = RegexInput(regex = RegularExpressions.password, isPure = true)
 * ) : FormzMixin {
 *    override val inputs: List<FormzInput<*, *>> = listOf(username, password)
 * }
 * ```*/
interface FormzInterface {
    /** Whether the [FormzInput] values are all valid.*/
    val isValid: Boolean
        get() = Formz.validate(inputs)

    /** Whether the [FormzInput] values are not all valid.*/
    val isNotValid: Boolean
        get() = !isValid

    /** Whether all of the [FormzInput] are pure.*/
    val isPure: Boolean
        get() = Formz.isPure(inputs)

    /** Whether at least one of the [FormzInput]s is dirty.*/
    val isDirty: Boolean
        get() = !isPure

    /** Returns all [FormzInput] instances.
     *
     * Override this and give it all [FormzInput]s in your class that should be
     * validated automatically.*/
    val inputs: List<FormzInput<*, *>>
}