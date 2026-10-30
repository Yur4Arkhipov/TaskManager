package com.jacqulin.taskmanager.feature.home.presentation.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TaskCountWordTest {

    @Test
    fun `taskCountWord returns 'задача' for 1`() {
        assertEquals("задача", taskCountWord(1))
    }

    @Test
    fun `taskCountWord returns 'задачи' for 2`() {
        assertEquals("задачи", taskCountWord(2))
    }

    @Test
    fun `taskCountWord returns 'задачи' for 4`() {
        assertEquals("задачи", taskCountWord(4))
    }

    @Test
    fun `taskCountWord returns 'задач' for 5`() {
        assertEquals("задач", taskCountWord(5))
    }

    @Test
    fun `taskCountWord returns 'задач' for 11`() {
        assertEquals("задач", taskCountWord(11))
    }

    @Test
    fun `taskCountWord returns 'задача' for 21`() {
        assertEquals("задача", taskCountWord(21))
    }

    @Test
    fun `taskCountWord returns 'задачи' for 22`() {
        assertEquals("задачи", taskCountWord(22))
    }

    @Test
    fun `taskCountWord returns 'задач' for 25`() {
        assertEquals("задач", taskCountWord(25))
    }

    @Test
    fun `taskCountWord returns 'задача' for 101`() {
        assertEquals("задача", taskCountWord(101))
    }

    @Test
    fun `taskCountWord returns 'задач' for 111`() {
        assertEquals("задач", taskCountWord(111))
    }

    @Test
    fun `taskCountWord returns 'задач' for 0`() {
        assertEquals("задач", taskCountWord(0))
    }
}
