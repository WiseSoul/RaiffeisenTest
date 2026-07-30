package com.example.raiffeisentest.presentation.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.raiffeisentest.R
import com.example.raiffeisentest.presentation.theme.AppRed
import com.example.raiffeisentest.presentation.users.components.UserListContent
import kotlinx.coroutines.launch

/** Connects [UsersViewModel] state and actions to the stateless users content. */
@Composable
internal fun UsersScreen(
    usersViewModel: UsersViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by usersViewModel.uiState.collectAsStateWithLifecycle()

    UsersContent(
        uiState = uiState,
        onLastVisibleItemChanged = usersViewModel::onLastVisibleItemChanged,
        onRetryClick = usersViewModel::retry,
        modifier = modifier,
    )
}

/** Renders every state of the users feature without owning business state. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UsersContent(
    uiState: UsersUiState,
    onLastVisibleItemChanged: (Int) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val unavailableFeatureMessage = stringResource(R.string.feature_not_implemented)
    val showUnavailableFeatureMessage = {
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(unavailableFeatureMessage)
        }
        Unit
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.users_title),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = showUnavailableFeatureMessage) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.menu_content_description),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = showUnavailableFeatureMessage) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_content_description),
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = AppRed,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = showUnavailableFeatureMessage,
                containerColor = AppRed,
                contentColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_content_description),
                )
            }
        },
    ) { contentPadding ->
        when (uiState) {
            UsersUiState.Loading ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                            .testTag(USERS_LOADING_TEST_TAG),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }

            UsersUiState.Error ->
                ErrorContent(
                    onRetryClick = onRetryClick,
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                )

            is UsersUiState.Content ->
                UserListContent(
                    uiState = uiState,
                    onLastVisibleItemChanged = onLastVisibleItemChanged,
                    onRetryClick = onRetryClick,
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                )
        }
    }
}

@Composable
private fun ErrorContent(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(R.string.users_load_error))
        Button(onClick = onRetryClick) {
            Text(stringResource(R.string.try_again))
        }
    }
}

internal const val USERS_LOADING_TEST_TAG = "users_loading"
