import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectfilm_135.ui.navigation.BottomNavItem
import com.example.projectfilm_135.ui.view.film.DestinasiDetailFilm
import com.example.projectfilm_135.ui.view.film.DestinasiHomeFilm
import com.example.projectfilm_135.ui.view.film.DestinasiUpdateFilm
import com.example.projectfilm_135.ui.view.film.DetailFilmView
import com.example.projectfilm_135.ui.view.film.EntryFilmScreen
import com.example.projectfilm_135.ui.view.film.FilmEntry
import com.example.projectfilm_135.ui.view.film.HomeScreen
import com.example.projectfilm_135.ui.view.film.UpdateFilmScreen
import com.example.projectfilm_135.ui.view.penayangan.DestinasiDetailPenayangan
import com.example.projectfilm_135.ui.view.penayangan.DestinasiHomePenayangan
import com.example.projectfilm_135.ui.view.penayangan.DestinasiUpdatePenayangan
import com.example.projectfilm_135.ui.view.penayangan.DetailPenayanganView
import com.example.projectfilm_135.ui.view.penayangan.EntryPenayanganScreen
import com.example.projectfilm_135.ui.view.penayangan.HomeScreenPenayangan
import com.example.projectfilm_135.ui.view.penayangan.PenayanganEntry
import com.example.projectfilm_135.ui.view.penayangan.UpdatePenayanganScreen
import com.example.projectfilm_135.ui.view.studio.DestinasiDetailStudio
import com.example.projectfilm_135.ui.view.studio.DestinasiHomeStudio
import com.example.projectfilm_135.ui.view.studio.DestinasiUpdateStudio
import com.example.projectfilm_135.ui.view.studio.DetailStudioView
import com.example.projectfilm_135.ui.view.studio.EntryStudioScreen
import com.example.projectfilm_135.ui.view.studio.HomeStudioScreen
import com.example.projectfilm_135.ui.view.studio.StudioEntry
import com.example.projectfilm_135.ui.view.studio.UpdateStudioScreen
import com.example.projectfilm_135.ui.view.tiket.DestinasiDetailTiket
import com.example.projectfilm_135.ui.view.tiket.DestinasiHomeTiket
import com.example.projectfilm_135.ui.view.tiket.DetailTiketView
import com.example.projectfilm_135.ui.view.tiket.HomeScreenTiket
import com.example.projectfilm_135.ui.view.tiket.InsertTiketView
import com.example.projectfilm_135.ui.view.tiket.TiketEntry

@Composable
fun PengelolaHalamanFilm(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    // Daftar menu bottom navigation
    val bottomNavItems = listOf(
        BottomNavItem.Home,         // Menu untuk Daftar Film
        BottomNavItem.HomePenayangan,
        BottomNavItem.HomeStudio,
        BottomNavItem.HomeTiket// Menu untuk Penayangan & Studio
    )

    // Mengatur UI dengan Bottom Navigation
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route, // Halaman awal (Daftar Film)
            modifier = Modifier.padding(innerPadding)
        ) {
            // Halaman Daftar Film (Home)
            composable(DestinasiHomeFilm.route) {
                HomeScreen(
                    navigateToItemEntry = {
                        navController.navigate(FilmEntry.route)
                    },
                    onDetailClick = { idFilm ->
                        navController.navigate("${DestinasiDetailFilm.route}/$idFilm")
                    }
                )
            }

            // Halaman Entry Film
            composable(FilmEntry.route) {
                EntryFilmScreen(navigateBack = {
                    navController.popBackStack(DestinasiHomeFilm.route, inclusive = false)
                })
            }



            // Halaman Detail Film
            composable(
                route = "${DestinasiDetailFilm.route}/{idFilm}",
                arguments = listOf(navArgument("idFilm") { type = NavType.IntType })
            ) { backStackEntry ->
                val idFilm = backStackEntry.arguments?.getInt("idFilm") ?: 0
                DetailFilmView(
                    idFilm = idFilm,
                    navigateBack = { navController.popBackStack() },
                    onClick = {
                        navController.navigate("${DestinasiUpdateFilm.route}/$idFilm")
                    }
                )
            }

            // Halaman Update Film
            composable(
                route = "${DestinasiUpdateFilm.route}/{idFilm}",
                arguments = listOf(navArgument("idFilm") { type = NavType.IntType })
            ) { backStackEntry ->
                val idFilm = backStackEntry.arguments?.getInt("idFilm") ?: 0
                UpdateFilmScreen(
                    idFilm = idFilm,
                    navigateBack = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(BottomNavItem.Home.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            // Halaman Penayangan & Studio (Schedule)
            composable(DestinasiHomeStudio.route) {
                HomeStudioScreen(
                    navigateToItemEntry = {
                        navController.navigate(StudioEntry.route)
                    },
                    onDetailClick = { idStudio ->
                        navController.navigate("${DestinasiDetailStudio.route}/$idStudio")
                    }
                ) // Halaman ini akan menampilkan jadwal dan studio
            }

            composable(StudioEntry.route) {
                EntryStudioScreen(navigateBack = {
                    navController.popBackStack(DestinasiHomeStudio.route, inclusive = false)
                })
            }

            composable(
                route = "${DestinasiDetailStudio.route}/{idStudio}",
                arguments = listOf(navArgument("idStudio") { type = NavType.IntType })
            ) { backStackEntry ->
                val idStudio = backStackEntry.arguments?.getInt("idStudio") ?: 0
                DetailStudioView(
                    idStudio = idStudio,
                    navigateBack = { navController.popBackStack() },
                    onClick = {
                        navController.navigate("${DestinasiUpdateStudio.route}/$idStudio")
                    }
                )
            }

            composable(
                route = "${DestinasiUpdateStudio.route}/{idStudio}",
                arguments = listOf(navArgument("idStudio") { type = NavType.IntType })
            ) { backStackEntry ->
                val idStudio = backStackEntry.arguments?.getInt("idStudio") ?: 0
                UpdateStudioScreen(
                    idStudio = idStudio,
                    navigateBack = {
                        navController.navigate(BottomNavItem.HomeStudio.route) {
                            popUpTo(BottomNavItem.HomeStudio.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            // Halaman Daftar PENAYANGAN (Home)
            composable(DestinasiHomePenayangan.route) {
                HomeScreenPenayangan(
                    navigateToItemEntry = {
                        navController.navigate(PenayanganEntry.route)
                    },
                    onDetailClick = { idPenayangan ->
                        navController.navigate("${DestinasiDetailPenayangan.route}/$idPenayangan")
                    }
                )
            }

            composable(PenayanganEntry .route) {
                EntryPenayanganScreen(navigateBack = {
                    navController.popBackStack(DestinasiHomePenayangan.route, inclusive = false)
                })
            }

            // Halaman Daftar PENAYANGAN (Home)
            composable(
                route = "${DestinasiDetailPenayangan.route}/{idPenayangan}",
                arguments = listOf(navArgument("idPenayangan") { type = NavType.IntType })
            ) { backStackEntry ->
                val idPenayangan = backStackEntry.arguments?.getInt("idPenayangan") ?: 0
                Log.d("DetailPenayangan", "Navigating to detail page with idPenayangan: $idPenayangan")

                DetailPenayanganView(
                    idPenayangan = idPenayangan,
                    navigateBack = { navController.popBackStack() },
                    onClick = {
                        Log.d("DetailPenayangan", "Navigating to update page for idPenayangan: $idPenayangan")
                        navController.navigate("${DestinasiUpdatePenayangan.route}/$idPenayangan")
                    }
                )
            }


            composable(
                route = "${DestinasiUpdatePenayangan.route}/{idPenayangan}",
                arguments = listOf(navArgument("idPenayangan") { type = NavType.IntType })
            ) { backStackEntry ->
                val idPenayangan = backStackEntry.arguments?.getInt("idPenayangan") ?: 0
                UpdatePenayanganScreen(
                    idPenayangan = idPenayangan,
                    navigateBack = {
                        navController.navigate(BottomNavItem.HomePenayangan.route) {
                            popUpTo(BottomNavItem.HomePenayangan.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            composable(DestinasiHomeTiket.route) {
                HomeScreenTiket(
                    navigateToItemEntry = {
                        navController.navigate(TiketEntry.route)
                    },
                    onDetailClick = { idPenayangan ->
                        navController.navigate("${DestinasiDetailPenayangan.route}/$idPenayangan")
                    }
                )
            }

            composable(TiketEntry.route) {
                InsertTiketView(navigateBack = {
                    navController.popBackStack(DestinasiHomeTiket.route, inclusive = false)
                })
            }

            composable(
                route = "${DestinasiDetailTiket.route}/{idTiket}",
                arguments = listOf(navArgument("idTiket") { type = NavType.IntType })
            ) { backStackEntry ->
                val idTiket = backStackEntry.arguments?.getInt("idTiket") ?: 0
                DetailTiketView(
                    idTiket = idTiket,
                    navigateBack = { navController.popBackStack() },
                    onClick = {
                        navController.navigate("")
                    }
                )
            }

        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


