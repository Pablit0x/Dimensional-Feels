package com.ps.dimensional_feels.navigation

import com.ps.dimensional_feels.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY_DIARY_ID

sealed class Screen(val route: String) {
    data object Authentication : Screen(route = Routes.AUTHENTICATION)
    data object Home : Screen(route = Routes.HOME)
    data object Write : Screen(route = Routes.WRITE_WITH_ARGS) {
        fun passDiaryId(diaryId: String) =
            "${Routes.WRITE}?$WRITE_SCREEN_ARGUMENT_KEY_DIARY_ID=$diaryId"
    }

    data object Draw : Screen(route = Routes.DRAW)
}