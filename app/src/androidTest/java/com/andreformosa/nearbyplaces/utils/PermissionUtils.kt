package com.andreformosa.nearbyplaces.utils

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

/**
 * Helper function which clicks on the positive action for permission system dialogs
 * Caveat: Only works for English localisation
 */
fun grantPermission() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val allowPermission = UiDevice.getInstance(instrumentation).findObject(
        UiSelector().text(
            when {
                Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                else -> "While using the app"
            }
        )
    )
    if (allowPermission.exists()) {
        allowPermission.click()
    }
}

/**
 * Helper function which clicks on the negative action for permission system dialogs
 * Caveat: Only works for English localisation
 */
fun denyPermission() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()

    val denyPermission = UiDevice.getInstance(instrumentation).findObject(
        UiSelector().text(
            when (Build.VERSION.SDK_INT) {
                in 24..28 -> "DENY"
                else -> "Don’t allow"
            }
        )
    )
    if (denyPermission.exists()) {
        denyPermission.click()
    }
}
