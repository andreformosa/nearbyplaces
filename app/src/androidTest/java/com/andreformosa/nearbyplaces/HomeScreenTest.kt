package com.andreformosa.nearbyplaces

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.andreformosa.nearbyplaces.robot.robot
import com.andreformosa.nearbyplaces.utils.denyPermission
import com.andreformosa.nearbyplaces.utils.grantPermission
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testPermissionRequestGrantedFlow() {
        // Automate flow to show permission request screen and click allow access button
        robot(composeTestRule) {
            requestLocationPermissionRobot {
                clickAllowLocationAccess()
            }
        }

        // Grant permission using system dialog
        grantPermission()

        // Assert that flow is finished since map is displayed after permission is granted
        composeTestRule.onNodeWithTag("map").assertIsDisplayed()
    }

    @Test
    fun testPermissionRequestDeniedFlow() {
        // Assert permission required text is displayed
        val permissionRequiredText =
            composeTestRule.activity.getString(R.string.permission_text_location_permission_required)
        composeTestRule.onNodeWithText(permissionRequiredText).assertIsDisplayed()

        // Automate flow to show permission request screen and click allow access button
        robot(composeTestRule) {
            requestLocationPermissionRobot {
                clickAllowLocationAccess()
            }
        }

        // Deny permission using system dialog
        denyPermission()

        // Assert permission rationale text is displayed (changes from original)
        val rationaleText =
            composeTestRule.activity.getString(R.string.permission_text_location_permission_required_rationale)
        composeTestRule.onNodeWithText(rationaleText).assertIsDisplayed()
    }

    @Test
    fun testPermissionRequestContent() {
        // Assert icon is displayed
        composeTestRule.onNodeWithContentDescription("location icon").assertIsDisplayed()

        // Assert permission required text is displayed
        val permissionRequiredText =
            composeTestRule.activity.getString(R.string.permission_text_location_permission_required)
        composeTestRule.onNodeWithText(permissionRequiredText).assertIsDisplayed()

        // Assert button is displayed
        val buttonText = composeTestRule.activity.getString(R.string.permission_button_allow_access)
        composeTestRule.onNodeWithText(buttonText).assertIsDisplayed()
    }
}
