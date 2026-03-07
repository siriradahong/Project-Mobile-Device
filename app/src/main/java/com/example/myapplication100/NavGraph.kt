package com.example.myapplication100


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "queue"
    ) {

        // ================= QUEUE SCREEN =================
        composable("queue") {

            QueueControlScreen(
                nav = navController
            )
        }

        // ================= VITALS SCREEN =================
        composable(
            route = "vitals/{appointmentId}/{queue}/{name}"
        ) { backStackEntry ->

            val appointmentId =
                backStackEntry.arguments?.getString("appointmentId")?.toInt() ?: 0

            val queueNumber =
                backStackEntry.arguments?.getString("queue") ?: ""

            val patientName =
                backStackEntry.arguments?.getString("name") ?: ""

            VitalsScreen(
                nav = navController,
                appointmentId = appointmentId,
                queueNumber = queueNumber,
                patientName = patientName
            )

        }
    }
}