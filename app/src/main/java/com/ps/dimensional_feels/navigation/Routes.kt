package com.ps.dimensional_feels.navigation

import com.ps.dimensional_feels.navigation.NavigationArguments.CALENDAR_SCREEN_ARGUMENT_KEY
import com.ps.dimensional_feels.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY

object Routes {
    const val AUTHENTICATION = "authentication_screen"
    const val HOME = "home_screen"
    const val WRITE = "write_screen"
    const val WRITE_WITH_ARGS = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}"
    const val CALENDAR = "calendar_screen"
    const val CALENDAR_WITH_ARGS = "calendar_screen?$CALENDAR_SCREEN_ARGUMENT_KEY={$CALENDAR_SCREEN_ARGUMENT_KEY}"
}