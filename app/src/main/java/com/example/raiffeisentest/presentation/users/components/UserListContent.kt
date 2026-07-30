package com.example.raiffeisentest.presentation.users.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.raiffeisentest.R
import com.example.raiffeisentest.domain.model.User
import com.example.raiffeisentest.presentation.users.UsersUiState
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun UserListContent(
    uiState: UsersUiState.Content,
    onLastVisibleItemChanged: (Int) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .distinctUntilChanged()
            .collect(onLastVisibleItemChanged)
    }

    LazyColumn(
        state = listState,
        modifier = modifier.testTag(USERS_LIST_TEST_TAG),
    ) {
        items(
            items = uiState.users,
            key = User::id,
        ) { user ->
            UserItem(user)
        }

        if (uiState.isLoadingMore) {
            item { LoadingMoreItem() }
        }

        if (uiState.hasLoadMoreError) {
            item { LoadMoreErrorItem(onRetryClick) }
        }
    }
}

@Composable
private fun UserItem(user: User) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = stringResource(R.string.user_avatar_content_description, user.fullName),
            modifier = Modifier.size(48.dp).clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = stringResource(R.string.user_age_and_nationality, user.age, user.nationality),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = stringResource(R.string.user_registration_date, formatRegistrationDate(user.registeredAt)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}

@Composable
private fun LoadingMoreItem() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun LoadMoreErrorItem(onRetryClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.users_load_more_error),
            modifier = Modifier.weight(1f),
        )
        Button(onClick = onRetryClick) {
            Text(stringResource(R.string.retry))
        }
    }
}

/** Formats the API's ISO timestamp for display, preserving a readable fallback for malformed data. */
internal fun formatRegistrationDate(value: String): String =
    runCatching {
        DateTimeFormatter
            .ofPattern("MMM d, yyyy")
            .format(Instant.parse(value).atZone(ZoneId.systemDefault()))
    }.getOrDefault(value.take(10))

internal const val USERS_LIST_TEST_TAG = "users_list"
