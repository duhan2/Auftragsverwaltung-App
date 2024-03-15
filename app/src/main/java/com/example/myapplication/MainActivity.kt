package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme

class MainActivity : ComponentActivity() {

    private val kundeviewModel: KundeViewModel by lazy {
        ViewModelProvider(
            this,
            KundeViewModelFactory(KundeRepository(KundeDatabase.getDatabase(this).kundenDao()))
        )[KundeViewModel::class.java]
    }

    private val kategorieViewModel: KategorieViewModel by lazy {
        ViewModelProvider(
            this,
            KategorieViewModelFactory(
                KategorieRepository(
                    KategorieDatabase.getDatabase(this).kategorieDao()
                )
            )
        )[KategorieViewModel::class.java]
    }

    //Kein wirkliches ViewModel:D
    private var reparaturChanges = ReparaturChanges()

    private var kategorieChanges = KategorieChanges()

    @RequiresApi(34)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),// color = Color.White
                ) {

                    val navController = rememberNavController()

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination


                    Scaffold(
                        topBar = {

                            TopAppBar(
                                backgroundColor = MaterialTheme.colorScheme.onSecondary,
                                title = {
                                    //Ändert angezeigten Titel in Toppbar
                                    when (currentDestination?.route.toString()) {
                                        "home" -> Text(
                                            text = "Aufträge",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )

                                        "edit" -> Text(
                                            text = "Auftrag Eingabe",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )

                                        "reparaturen" -> Text(
                                            text = "Reparaturen",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )

                                        "reparatureingabe" -> Text(
                                            text = "Reparatureingabe",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )

                                        "kategorieeingabe" -> Text(
                                            text = "Kategorieeingabe",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )

                                        "auswahl" -> Text(
                                            text = "Reparatur Warenkorb",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )

                                        "auftrag" -> Text(
                                            text = "Auftrag ansehen",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                },
                                navigationIcon = {

                                    if (currentDestination?.route.toString() != "home") {
                                        IconButton(
                                            onClick = {
                                                //Wenn kein zuletzt Besuchter Screen, dann zurück zu home
                                                if (!navController.popBackStack()) {
                                                    //Eventuell ViewModel zurücksetzen
                                                    navController.navigate("home")
                                                }
                                            }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = "Backbutton in Editmenu"
                                            )
                                        }
                                    }
                                }

                            )
                        },


                        bottomBar = {
                            BottomNavigation(
                                backgroundColor = MaterialTheme.colorScheme.onSecondary
                            ) {
                                BottomNavigationItem(
                                    label = { Text("Aufträge") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "home" } == true,
                                    onClick = {
                                        navController.navigate("home") {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            //eventuell probleme
                                            popUpTo("home") {
                                                saveState = false
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Home,
                                            contentDescription = null
                                        )
                                    })

                                BottomNavigationItem(
                                    label = { Text("Reparaturen") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "reparaturen" } == true,
                                    onClick = {
                                        navController.navigate("reparaturen") {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            //eventuell probleme
                                            popUpTo("reparaturen") {
                                                saveState = false
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Build,
                                            contentDescription = null
                                        )
                                    })

                            }
                        },
                        floatingActionButton = {

                            when (currentDestination?.route.toString()) {
                                "home" -> LargeFloatingActionButton(shape = CircleShape, onClick = {

                                    //Edit Screen nicht prepopulaten
                                    reparaturChanges.resetchanges()

                                    navController.navigate("edit")
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }

                                "reparaturen" -> LargeFloatingActionButton(
                                    shape = CircleShape,
                                    onClick = {

                                        kategorieChanges.resetchanges()

                                        navController.navigate("reparatureingabe")
                                    }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }

                        }, containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) { innerPadding ->
                        println(currentDestination?.route.toString())

                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            Modifier.padding(innerPadding)
                        ) {

                            composable("home") {

                                Homescreen(
                                    kundeViewModel = kundeviewModel,
                                    navController = navController,
                                    stagedReparaturChanges = reparaturChanges
                                )

                            }

                            composable("reparaturen") {
                                ReparaturScreen(
                                    kategorieViewModel = kategorieViewModel,
                                    navController = navController,
                                    kategorieChanges = kategorieChanges
                                )
                            }

                            composable("edit") {
                                Editscreen(
                                    kundeViewModel = kundeviewModel,
                                    navController = navController,
                                    stagedReparaturChanges = reparaturChanges
                                )

                            }

                            composable("reparatureingabe") {
                                ReparaturEingabeScreen(
                                    kategorieViewModel = kategorieViewModel,
                                    navController = navController,
                                    kategorieChanges = kategorieChanges
                                )
                            }

                            composable("kategorieeingabe") {
                                KategorieEingabeScreen(
                                    kategorieViewModel = kategorieViewModel,
                                    navController = navController,
                                    kategorieChanges = kategorieChanges
                                )
                            }

                            composable("auswahl") {
                                AuswahlScreen(
                                    navController = navController,
                                    kategorieViewModel = kategorieViewModel,
                                    stagedReparaturChanges = reparaturChanges
                                )
                            }

                            composable("auftrag") {
                                AuftragScreen(
                                    reparaturChanges = reparaturChanges,
                                    kundeViewModel = kundeviewModel
                                )
                            }

                        }
                    }


                }
            }
        }

    }
}

