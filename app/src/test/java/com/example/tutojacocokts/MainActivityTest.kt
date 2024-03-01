package com.example.tutojacocokts

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.example.tutojacocokts.ui.theme.TutoJacocoKtsTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalTestApi::class)
class MainActivityTest {

    @Test
    fun test() = runComposeUiTest {
        setContent {
            TutoJacocoKtsTheme {
                Greeting(name = "toto")
            }
        }
        onNodeWithText("Hello toto!").assertIsDisplayed()
    }
}
