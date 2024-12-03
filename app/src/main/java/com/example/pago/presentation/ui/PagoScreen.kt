@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.pago.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pago.R
import com.example.pago.presentation.state.PersonItem
import com.example.pago.presentation.state.UiState
import com.example.pago.presentation.ui.Navigation.Args.PERSON_EMAIL
import com.example.pago.presentation.ui.Navigation.Args.PERSON_ID
import com.example.pago.presentation.ui.Navigation.Args.PERSON_NAME

object Navigation {
    object Args {
        const val PERSON_ID = "PERSON_ID"
        const val PERSON_NAME = "PERSON_NAME"
        const val PERSON_EMAIL = "PERSON_EMAIL"
    }

    const val START_ROUTE = "Home"
    const val PERSON_DETAILS_ROUTE = "PersonDetails"

    sealed class Route(val ordinal: Int, val route: String) {
        data object Start : Route(0, START_ROUTE)
        data object PersonDetails :
            Route(1, "$PERSON_DETAILS_ROUTE/{$PERSON_ID}/{$PERSON_NAME}/{$PERSON_EMAIL}") {
            fun createRoute(personId: Int, personName: String, personEmail: String) =
                "PersonDetails/$personId/$personName/$personEmail"

            fun createArgumentTypes() = listOf(
                navArgument(PERSON_ID) { type = NavType.IntType },
                navArgument(PERSON_NAME) { type = NavType.StringType },
                navArgument(PERSON_EMAIL) { type = NavType.StringType }
            )
        }

        companion object {
            fun findOrdinalByRoute(route: String?): Int {
                return when {
                    route.isNullOrEmpty() -> 0
                    route.startsWith(START_ROUTE) -> 0
                    route.startsWith(PERSON_DETAILS_ROUTE) -> 1
                    else -> 0
                }
            }
        }
    }
}

@Composable
fun PagoAppBar(
    currentScreenName: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = currentScreenName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun PagoApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route
    val screenOrdinal = Navigation.Route.findOrdinalByRoute(currentRoute)
    val screenName = when (screenOrdinal) {
        Navigation.Route.Start.ordinal -> Navigation.Route.Start.route
        Navigation.Route.PersonDetails.ordinal ->
            backStackEntry?.arguments?.run { getString(PERSON_NAME) } ?: "Details"

        else -> ""
    }

    Scaffold(
        topBar = {
            PagoAppBar(
                currentScreenName = screenName,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        val homeViewModel: HomeViewModel = hiltViewModel()
        val uiState: State<UiState<List<PersonItem>>> =
            homeViewModel.personsState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Navigation.Route.Start.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(
                route = Navigation.Route.Start.route
            ) {
                when (val state = uiState.value) {
                    is UiState.Loading -> {
                        MainScreenLoading()
                    }

                    is UiState.Loaded -> {
                        MainScreen(
                            state.data,
                            onClickRefresh = { homeViewModel.refresh() }) {
                            val route =
                                Navigation.Route.PersonDetails.createRoute(it.id, it.name, it.email)
                            navController.navigate(route)
                        }
                    }

                    is UiState.Error -> {
                        MainScreenErrorLoading()
                    }
                }

            }
            composable(
                route = Navigation.Route.PersonDetails.route,
                arguments = Navigation.Route.PersonDetails.createArgumentTypes()
            ) {
                val personId = backStackEntry?.arguments?.run { getInt(PERSON_ID) } ?: -1
                val personName = backStackEntry?.arguments?.run { getString(PERSON_NAME) } ?: ""
                val personEmail = backStackEntry?.arguments?.run { getString(PERSON_EMAIL) } ?: ""
                PersonDetailsScreen(
                    id = personId,
                    personName = personName,
                    personEmail = personEmail
                )
            }
        }
    }
}

@Composable
fun MainScreenLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun MainScreenErrorLoading() {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            color = Color.Red,
            text = stringResource(
                R.string.person_posts_error_loading
            ),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}