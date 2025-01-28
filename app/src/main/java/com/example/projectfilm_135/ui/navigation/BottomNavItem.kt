package com.example.projectfilm_135.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

// Data untuk setiap menu di Bottom Navigation
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_film", "Daftar Film", Icons.Default.Home)
    object HomePenayangan : BottomNavItem("home_penayangan","Penayangan", Icons.Default.DateRange)
    object HomeStudio : BottomNavItem("home_studio", "Studio", Icons.Default.ExitToApp)
    object HomeTiket : BottomNavItem("home_tiket", "Beli Tiket", Icons.Default.ShoppingCart)
}