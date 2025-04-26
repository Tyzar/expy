package com.nokotogi.android.expy.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nokotogi.android.expy.ui.routes.RootRoute
import com.nokotogi.android.expy.ui.routes.rootNavGraph

@Composable
fun App() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = RootRoute) {
        rootNavGraph(rootNavController)
    }
}