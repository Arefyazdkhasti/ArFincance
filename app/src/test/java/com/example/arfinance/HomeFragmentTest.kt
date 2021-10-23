package com.example.arfinance

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.arfinance.ui.home.HomeFragment
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import java.util.*

private const val FAKE_DATE = "10/15/2021"

/*

class HomeFragmentTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Mock
    lateinit var homeFragment: HomeFragment
    @Mock
    val calendar =Calendar.getInstance()

    @Before
    fun setUp(){
        homeFragment = HomeFragment()
    }



    @Test
    fun test_formatDate(){
        calendar.set(Calendar.YEAR, 2021)
        calendar.set(Calendar.MONTH, 10)
        calendar.set(Calendar.DAY_OF_MONTH, 15)
        val result = homeFragment.formatDate(calendar.time)

        assertThat(result, `is`(FAKE_DATE))

    }

}*/
