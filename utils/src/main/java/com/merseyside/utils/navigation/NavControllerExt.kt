package com.merseyside.utils.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.isTopLevelDestination(): Boolean {
    return graph.findStartDestination() == currentDestination
}