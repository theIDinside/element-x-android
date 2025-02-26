/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.features.createroom.impl.root

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.element.android.features.createroom.impl.R
import io.element.android.features.userlist.api.components.UserListView
import io.element.android.libraries.architecture.Async
import io.element.android.libraries.designsystem.components.ProgressDialog
import io.element.android.libraries.designsystem.components.dialogs.RetryDialog
import io.element.android.libraries.designsystem.preview.ElementPreviewDark
import io.element.android.libraries.designsystem.preview.ElementPreviewLight
import io.element.android.libraries.designsystem.theme.components.CenterAlignedTopAppBar
import io.element.android.libraries.designsystem.theme.components.Icon
import io.element.android.libraries.designsystem.theme.components.IconButton
import io.element.android.libraries.designsystem.theme.components.Scaffold
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.matrix.api.core.RoomId
import io.element.android.libraries.designsystem.R as DrawableR
import io.element.android.libraries.ui.strings.R as StringR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomRootView(
    state: CreateRoomRootState,
    modifier: Modifier = Modifier,
    onClosePressed: () -> Unit = {},
    onNewRoomClicked: () -> Unit = {},
    onOpenDM: (RoomId) -> Unit = {},
) {
    if (state.startDmAction is Async.Success) {
        LaunchedEffect(state.startDmAction) {
            onOpenDM(state.startDmAction.state)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            if (!state.userListState.isSearchActive) {
                CreateRoomRootViewTopBar(onClosePressed = onClosePressed)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val context = LocalContext.current
            UserListView(
                modifier = Modifier.fillMaxWidth(),
                state = state.userListState,
                onUserSelected = {
                    // Fixme disabled DM creation since it can break the account data which is not correctly synced
                    //  uncomment to enable it again or move behind a feature flag
                    Toast.makeText(context, "Create DM feature is disabled.", Toast.LENGTH_SHORT).show()
//                    state.eventSink(CreateRoomRootEvents.StartDM(it))
                },
            )

            if (!state.userListState.isSearchActive) {
                CreateRoomActionButtonsList(
                    onNewRoomClicked = onNewRoomClicked,
                    onInvitePeopleClicked = { state.eventSink(CreateRoomRootEvents.InvitePeople) },
                )
            }
        }
    }

    when (state.startDmAction) {
        is Async.Loading -> {
            ProgressDialog(text = stringResource(id = StringR.string.common_creating_room))
        }
        is Async.Failure -> {
            RetryDialog(
                content = stringResource(id = R.string.screen_start_chat_error_starting_chat),
                onDismiss = { state.eventSink(CreateRoomRootEvents.CancelStartDM) },
                onRetry = {
                    state.userListState.selectedUsers.firstOrNull()
                        ?.let { state.eventSink(CreateRoomRootEvents.StartDM(it)) }
                    // Cancel start DM if there is no more selected user (should not happen)
                        ?: state.eventSink(CreateRoomRootEvents.CancelStartDM)
                },
            )
        }
        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomRootViewTopBar(
    modifier: Modifier = Modifier,
    onClosePressed: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = StringR.string.action_start_chat),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        },
        actions = {
            IconButton(onClick = onClosePressed) {
                Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(id = StringR.string.action_close))
            }
        }
    )
}

@Composable
fun CreateRoomActionButtonsList(
    modifier: Modifier = Modifier,
    onNewRoomClicked: () -> Unit = {},
    onInvitePeopleClicked: () -> Unit = {},
) {
    Column(modifier = modifier) {
        CreateRoomActionButton(
            iconRes = DrawableR.drawable.ic_groups,
            text = stringResource(id = StringR.string.action_create_a_room),
            onClick = onNewRoomClicked,
        )
        CreateRoomActionButton(
            iconRes = DrawableR.drawable.ic_share,
            text = stringResource(id = R.string.screen_create_room_action_invite_people),
            onClick = onInvitePeopleClicked,
        )
    }
}

@Composable
fun CreateRoomActionButton(
    @DrawableRes iconRes: Int,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.alpha(0.5f), // FIXME align on Design system theme (removing alpha should be fine)
            resourceId = iconRes,
            contentDescription = null,
        )
        Text(text = text)
    }
}

@Preview
@Composable
fun CreateRoomRootViewLightPreview(@PreviewParameter(CreateRoomRootStateProvider::class) state: CreateRoomRootState) =
    ElementPreviewLight { ContentToPreview(state) }

@Preview
@Composable
fun CreateRoomRootViewDarkPreview(@PreviewParameter(CreateRoomRootStateProvider::class) state: CreateRoomRootState) =
    ElementPreviewDark { ContentToPreview(state) }

@Composable
private fun ContentToPreview(state: CreateRoomRootState) {
    CreateRoomRootView(
        state = state,
    )
}
