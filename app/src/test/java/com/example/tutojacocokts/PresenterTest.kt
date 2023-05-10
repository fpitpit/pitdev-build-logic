package com.example.tutojacocokts

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PresenterTest {
    private lateinit var presenter: Presenter

    @Before
    fun setUp() {
        presenter = Presenter()
    }
    @Test
    fun testHello() {
        assertEquals("Hello toi", presenter.hello("toi"))
    }
}