package com.ps.happydays.navigation

import com.ps.happydays.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY

object Routes {
    const val AUTHENTICATION = "authentication_screen"
    const val HOME = "home_screen"
    const val WRITE = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}"
}