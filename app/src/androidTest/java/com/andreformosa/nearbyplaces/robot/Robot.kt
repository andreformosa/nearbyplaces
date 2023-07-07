package com.andreformosa.nearbyplaces.robot

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import org.junit.rules.TestRule

class Robot<R : TestRule, A : ComponentActivity>(private val testRule: AndroidComposeTestRule<R, A>) {

    fun requestLocationPermissionRobot(action: @RestrictScope RequestLocationPermissionRobot<R, A>.() -> Unit) =
        RequestLocationPermissionRobot(testRule).action()
}

fun <R : TestRule, A : ComponentActivity> robot(
    testRule: AndroidComposeTestRule<R, A>,
    action: Robot<R, A>.() -> Unit,
) = Robot(testRule).action()
