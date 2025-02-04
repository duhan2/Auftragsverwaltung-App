package com.example.myapplication

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.myapplication.archiv.ArchivChanges
import com.example.myapplication.archiv.ArchivDatabase
import com.example.myapplication.archiv.ArchivRepository
import com.example.myapplication.archiv.ArchivViewModel
import com.example.myapplication.archiv.ArchivViewModelFactory
import com.example.myapplication.kategorie.KategorieChanges
import com.example.myapplication.kategorie.KategorieDatabase
import com.example.myapplication.kategorie.KategorieRepository
import com.example.myapplication.kategorie.KategorieViewModel
import com.example.myapplication.kategorie.KategorieViewModelFactory
import com.example.myapplication.kunde.KundeDatabase
import com.example.myapplication.kunde.KundeRepository
import com.example.myapplication.kunde.KundeViewModel
import com.example.myapplication.kunde.KundeViewModelFactory
import com.example.myapplication.reparatur.ReparaturChanges
import com.example.myapplication.screens.ArchivAnsichtScreen
import com.example.myapplication.screens.ArchivScreen
import com.example.myapplication.screens.AuftragScreen
import com.example.myapplication.screens.AuswahlScreen
import com.example.myapplication.screens.Editscreen
import com.example.myapplication.screens.Homescreen
import com.example.myapplication.screens.KategorieEingabeScreen
import com.example.myapplication.screens.KundenAnsichtScreen
import com.example.myapplication.screens.ReparaturEingabeScreen
import com.example.myapplication.screens.ReparaturScreen

class MainActivity : ComponentActivity() {

    private val kundeViewModel: KundeViewModel by lazy {
        ViewModelProvider(
            this,
            KundeViewModelFactory(KundeRepository(KundeDatabase.getDatabase(this).kundenDao()))
        )[KundeViewModel::class.java]
    }

    private val kategorieViewModel: KategorieViewModel by lazy {
        ViewModelProvider(
            this, KategorieViewModelFactory(
                KategorieRepository(
                    KategorieDatabase.getDatabase(this).kategorieDao()
                )
            )
        )[KategorieViewModel::class.java]
    }

    private val archivViewModel: ArchivViewModel by lazy {
        ViewModelProvider(
            this,
            ArchivViewModelFactory(ArchivRepository(ArchivDatabase.getDatabase(this).archivDao()))
        )[ArchivViewModel::class.java]
    }

    //Klasse die Daten für die Screenlogik hält
    private var reparaturChanges = ReparaturChanges()

    private var kategorieChanges = KategorieChanges()

    private var archivChanges = ArchivChanges()

    @RequiresApi(34)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {

                    val navController = rememberNavController()

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    //Die Listen hier erschaffen, damit bei wechseln von Tab liste nicht immer neu erschaffen wird
                    val kundenliste = kundeViewModel.getallKunden().observeAsState(listOf())
                    val kategorieliste =
                        kategorieViewModel.getallKategorien().observeAsState(listOf())
                    val archivliste = archivViewModel.getallArchiv().observeAsState(listOf())

                    Scaffold(topBar = {

                        TopAppBar(
                            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            title = {
                                //Ändert angezeigten Titel in Toppbar
                                when (currentDestination?.route.toString()) {
                                    "home" -> Text(
                                        text = "Aufträge",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "edit" -> Text(
                                        text = "Auftrag Eingabe",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "reparaturen" -> Text(
                                        text = "Reparaturen",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "reparatureingabe" -> Text(
                                        text = "Reparatureingabe",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "kategorieeingabe" -> Text(
                                        text = "Kategorieeingabe",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "auswahl" -> Text(
                                        text = "Reparatur Warenkorb",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "auftrag" -> Text(
                                        text = "Auftrag ansehen",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "archiv" -> Text(
                                        text = "Kunden",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "archivansicht" -> Text(
                                        text = "Archivierter Auftrag",
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            },
                            navigationIcon = {

                                if (currentDestination?.route.toString() != "home") {
                                    IconButton(onClick = {
                                        //Wenn kein zuletzt Besuchter Screen, dann zurück zu home
                                        if (!navController.popBackStack()) {
                                            //Eventuell ViewModel zurücksetzen
                                            navController.navigate("home")
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            contentDescription = "Backbutton in Editmenu"
                                        )
                                    }
                                }
                            }

                        )
                    },


                        bottomBar = {
                            BottomNavigation(
                                backgroundColor = MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                BottomNavigationItem(label = { Text("Aufträge") },
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
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    })

                                BottomNavigationItem(label = { Text("Kunden") },
                                    selected = currentDestination?.hierarchy?.any { it.route == "archiv" } == true,
                                    onClick = {
                                        navController.navigate("archiv") {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            //eventuell probleme
                                            popUpTo("archiv") {
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
                                            Icons.Filled.AccountBox,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    })

                                BottomNavigationItem(label = { Text("Reparaturen") },
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
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    })

                            }
                        }, floatingActionButton = {

                            when (currentDestination?.route.toString()) {
                                "home" -> LargeFloatingActionButton(
                                    shape = CircleShape,
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    onClick = {

                                        //Edit Screen nicht prepopulaten
                                        reparaturChanges.resetchanges()
                                        reparaturChanges.editparam = "new"

                                        navController.navigate("edit")

                                    }) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier.size(66.dp)
                                    )
                                }

                                "reparaturen" -> LargeFloatingActionButton(shape = CircleShape,
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    onClick = {

                                        kategorieChanges.resetchanges()

                                        navController.navigate("reparatureingabe")
                                    }) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier.size(66.dp)
                                    )
                                }

                            }

                        }, containerColor = MaterialTheme.colorScheme.surface
                    ) { innerPadding ->
                        //println(currentDestination?.route.toString())

                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            Modifier.padding(innerPadding)
                        ) {

                            composable("home") {

                                Homescreen(
                                    kundeViewModel = kundeViewModel,
                                    navController = navController,
                                    stagedReparaturChanges = reparaturChanges,
                                    kundenliste = kundenliste
                                )

                            }

                            composable("reparaturen") {
                                ReparaturScreen(
                                    kategorieViewModel = kategorieViewModel,
                                    navController = navController,
                                    kategorieChanges = kategorieChanges,
                                    kategorieliste = kategorieliste
                                )
                            }

                            composable("edit") {
                                Editscreen(
                                    kundeViewModel = kundeViewModel,
                                    navController = navController,
                                    archivliste = archivliste,
                                    stagedReparaturChanges = reparaturChanges,
                                    archivViewModel = archivViewModel
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
                                    //kategorieViewModel = kategorieViewModel,
                                    stagedReparaturChanges = reparaturChanges,
                                    kategorieliste = kategorieliste
                                )
                            }

                            composable("auftrag") {
                                AuftragScreen(
                                    navController = navController,
                                    reparaturChanges = reparaturChanges,
                                    kundeViewModel = kundeViewModel,
                                    archivViewModel = archivViewModel,
                                    archivList = archivliste
                                )
                            }

                            composable("archiv") {
                                ArchivScreen(
                                    navController = navController,
                                    archivListState = archivliste,
                                    archivChanges = archivChanges
                                )
                            }
                            composable("archivansicht") {
                                ArchivAnsichtScreen(
                                    reparaturChanges = reparaturChanges
                                )
                            }

                            composable("kundenansicht") {
                                KundenAnsichtScreen(
                                    navController = navController,
                                    stagedReparaturChanges = reparaturChanges,
                                    archivViewModel = archivViewModel,
                                    archivChanges = archivChanges
                                )
                            }

                        }
                    }


                }
            }
        }

    }
}

