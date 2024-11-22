package com.example.vitesse.utils

object ValidationUtil {

    // Validate first name (non-empty)
    fun validateFirstName(firstName: String): String? {
        return if (firstName.isEmpty()) {
            "First name is required"
        } else {
            null
        }
    }

    // Validate last name (non-empty)
    fun validateLastName(lastName: String): String? {
        return if (lastName.isEmpty()) {
            "Last name is required"
        } else {
            null
        }
    }

    // Validate description (non-empty and only digits)
    fun validateDescription(description: String): String? {
        return when {
            description.isEmpty() -> "Description is required"
            !description.all { it.isDigit() } -> "Description must be numeric"
            else -> null
        }
    }

    // Validate phone number (must be 10 digits)
    fun validatePhone(phone: String): String? {
        return when {
            phone.isEmpty() -> "Phone number is required"
            !phone.matches(Regex("^\\d{10}$")) -> "Phone number must be exactly 10 digits"
            else -> null
        }
    }

    // Validate email format
    fun validateEmail(email: String): String? {
        val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return when {
            email.isEmpty() -> "Email is required"
            !email.matches(emailPattern) -> "Invalid email format"
            else -> null
        }
    }

    // Validate date (non-empty)
    fun validateDate(date: String): String? {
        return if (date.isEmpty()) {
            "Date is required"
        } else {
            null
        }
    }

    fun validateNote(note: String): String? {
        return if (note.isEmpty()) {
            "Note is required"
        } else {
            null
        }
    }
}
