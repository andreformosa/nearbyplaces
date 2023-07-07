package com.andreformosa.nearbyplaces.robot

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andreformosa.nearbyplaces.utils.waitUntilShown
import org.junit.rules.TestRule

class RequestLocationPermissionRobot<R : TestRule, A : ComponentActivity>(private val testRule: AndroidComposeTestRule<R, A>) {

    fun clickAllowLocationAccess() {
        val buttonText =
            testRule.activity.getString(com.andreformosa.nearbyplaces.R.string.permission_button_allow_access)
        testRule.waitUntilShown {
            onNodeWithText(buttonText)
        }.performClick()
    }
}
