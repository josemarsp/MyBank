package com.example.mybank.feature.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginScreenTest {

    @Test
    fun `valid credentials pass validation`() {
        val email = "user@example.com"
        val password = "senha1234"
        assertTrue(isLoginInputValid(email, password))
    }

    @Test
    fun `empty fields fail validation`() {
        assertFalse(isLoginInputValid("", ""))
        assertFalse(isLoginInputValid("user@example.com", ""))
        assertFalse(isLoginInputValid("", "senha1234"))
    }

    @Test
    fun `short password fails validation`() {
        assertFalse(isLoginInputValid("user@example.com", "short"))
    }

    @Test
    fun `invalid email fails validation`() {
        assertFalse(isLoginInputValid("invalid-email", "senha1234"))
    }
}

