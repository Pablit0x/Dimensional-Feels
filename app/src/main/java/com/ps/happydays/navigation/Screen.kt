package com.ps.happydays.navigation

import com.ps.happydays.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    data object Authentication : Screen(route = Routes.AUTHENTICATION)
    data object Home : Screen(route = Routes.HOME)
    data object Write : Screen(route = Routes.WRITE) {
        fun passDiaryId(diaryId: String) = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=$diaryId"
    }
}