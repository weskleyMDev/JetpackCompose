package com.weskley.hdc_app.controller

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.weskley.hdc_app.screen.AlarmScreen
import com.weskley.hdc_app.screen.FeedbackScreen
import com.weskley.hdc_app.screen.HomeScreen
import com.weskley.hdc_app.screen.PrescriptionScreen
import com.weskley.hdc_app.screen.ProfileScreen
import com.weskley.hdc_app.screen.TreatmentScreen
import com.weskley.hdc_app.viewmodel.MedicineViewModel

const val MY_URI = "https://weskley.com"
const val MEDICINE = "medicine"
const val TIME = "time"
const val MEDICINE_ID = "id"
const val MEDICINE_AMOUNT = "amount"

@Composable
fun NavGraphController(
    navController: NavHostController,
) {
    val viewModel: MedicineViewModel = hiltViewModel()
    Log.d("NavGraphController", "ViewModel instance in NavGraphController: $viewModel, HashCode: ${System.identityHashCode(viewModel)}")
    val medicineEvent = viewModel::medicineEvent
    NavHost(
        navController = navController,
        startDestination = ScreenController.Home.route
    ) {
        composable(route = ScreenController.Home.route) {
            HomeScreen(navController)
        }
        composable(route = ScreenController.Alarm.route) {
            AlarmScreen(navController = navController)
        }
        composable(route = ScreenController.Prescription.route) {
            PrescriptionScreen()
        }
        composable(route = ScreenController.Profile.route) {
            ProfileScreen()
        }
        composable(route = ScreenController.Treatment.route) {
            TreatmentScreen()
        }
        composable(
            route = ScreenController.Feedback.route,
            arguments = listOf(
                navArgument(MEDICINE) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(TIME) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(MEDICINE_ID) {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(MEDICINE_AMOUNT) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "$MY_URI/$MEDICINE={$MEDICINE}&$TIME={$TIME}&$MEDICINE_ID={$MEDICINE_ID}&$MEDICINE_AMOUNT={$MEDICINE_AMOUNT}"
            })
        ) {
            val medicine = it.arguments?.getString(MEDICINE)
            val time = it.arguments?.getString(TIME)
            val id = it.arguments?.getInt(MEDICINE_ID)
            val amountStr = it.arguments?.getString(MEDICINE_AMOUNT)
            val amount = amountStr?.toIntOrNull()
            FeedbackScreen(medicine!!, time!!, id!!, amount!!)
        }
    }
}