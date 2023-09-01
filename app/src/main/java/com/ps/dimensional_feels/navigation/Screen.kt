package com.ps.dimensional_feels.navigation

import com.ps.dimensional_feels.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Authentication : Screen(route = Routes.AUTHENTICATION)
    object Home : Screen(route = Routes.HOME)
    object Write : Screen(route = Routes.WRITE_WITH_ARGS) {
        fun passDiaryId(diaryId: String) = "${Routes.WRITE}?$WRITE_SCREEN_ARGUMENT_KEY=$diaryId"
    }
}