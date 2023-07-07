package com.andreformosa.nearbyplaces.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.junit4.ComposeContentTestRule

/**
 * Helper function that waits until a composable is shown
 */
fun ComposeContentTestRule.waitUntilShown(
    timeoutMillis: Long = 10_000L,
    provider: SemanticsNodeInteractionsProvider.() -> SemanticsNodeInteraction,
) = provider().also {
    waitUntil(timeoutMillis) {
        try {
            it.assertExists()
            true
        } catch (e: AssertionError) {
            false
        }
    }
}
