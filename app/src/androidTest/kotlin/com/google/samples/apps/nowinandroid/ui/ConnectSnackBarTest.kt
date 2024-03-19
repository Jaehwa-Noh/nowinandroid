/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.ui

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.testharness.TestHarness
import com.google.samples.apps.nowinandroid.MainActivity
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.test.networkmonitor.AlwaysOfflineNetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.rules.GrantPostNotificationsPermissionRule
import com.google.samples.apps.nowinandroid.extensions.stringResource
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.inject.Inject

@HiltAndroidTest
class ConnectSnackBarTest {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Grant [android.Manifest.permission.POST_NOTIFICATIONS] permission.
     */
    @get:Rule(order = 2)
    val postNotificationsPermission = GrantPostNotificationsPermissionRule()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var userNewsResourceRepository: CompositeUserNewsResourceRepository

    @Inject
    lateinit var timeZoneMonitor: TimeZoneMonitor

    private val networkMonitor: NetworkMonitor = AlwaysOfflineNetworkMonitor()

    private val forYou by composeTestRule.stringResource(com.google.samples.apps.nowinandroid.feature.foryou.R.string.feature_foryou_title)
    private val interests by composeTestRule.stringResource(com.google.samples.apps.nowinandroid.feature.interests.R.string.feature_interests_title)
    private val saved by composeTestRule.stringResource(com.google.samples.apps.nowinandroid.feature.bookmarks.R.string.feature_bookmarks_title)
    private val netConnected by composeTestRule.stringResource(R.string.not_connected)

    @Before
    fun setup() = hiltRule.inject()

    enum class SnackBarTopPosition(val topPosition: Dp) {
        COMPACT(847.dp),
        MEDIUM(929.dp),
        EXPANDED(928.5.dp),
    }

    @RunPhoneSizeDevice
    @Test
    fun compactWidth_WhenNotConnectedAndForYou_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(400.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(forYou).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.COMPACT.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun compactWidth_WhenNotConnectedAndSaved_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(400.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(saved).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.COMPACT.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun compactWidth_WhenNotConnectedAndInterests_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(400.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(interests).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.COMPACT.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun mediumWidth_WhenNotConnectedAndForYou_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(610.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(forYou).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.MEDIUM.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun mediumWidth_WhenNotConnectedAndSaved_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(610.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(saved).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.MEDIUM.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun mediumWidth_WhenNotConnectedAndInterests_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(610.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(interests).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.MEDIUM.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun expandedWidth_WhenNotConnectedAndForYou_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(900.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(forYou).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.EXPANDED.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun expandedWidth_WhenNotConnectedAndSaved_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(900.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(saved).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.EXPANDED.topPosition)
        }
    }

    @RunPhoneSizeDevice
    @Test
    fun expandedWidth_WhenNotConnectedAndInterests_ConnectSnackBarShowUp() {
        composeTestRule.activity.apply {
            setContent {
                TestHarness(size = DpSize(900.dp, 1000.dp)) {
                    BoxWithConstraints {
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }

        composeTestRule.apply {
            findNavigationButton(interests).apply {
                performClick()
                assertIsSelected()
            }

            findSnackbarWithMessage(message = netConnected)
                .assertIsDisplayed()
                .assertTopPositionInRootIsEqualTo(SnackBarTopPosition.EXPANDED.topPosition)
        }
    }

    private fun findSnackbarWithMessage(message: String): SemanticsNodeInteraction =
        composeTestRule.onNode(
            matcher = hasTestTag("Snackbar") and
                hasAnyDescendant(matcher = hasText(message)),
        )

    private fun findNavigationButton(string: String): SemanticsNodeInteraction =
        composeTestRule.onNode(matcher = isSelectable() and hasText(string))

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    private fun fakeAppState(maxWidth: Dp, maxHeight: Dp) = rememberNiaAppState(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
        networkMonitor = networkMonitor,
        userNewsResourceRepository = userNewsResourceRepository,
        timeZoneMonitor = timeZoneMonitor,
    )
}

/**
 * This annotation class annotate
 * the test code only work when run on Phone size device.
 */
annotation class RunPhoneSizeDevice
