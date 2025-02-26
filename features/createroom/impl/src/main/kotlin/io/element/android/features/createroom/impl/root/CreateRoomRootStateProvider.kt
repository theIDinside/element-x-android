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

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.features.userlist.api.aUserListState
import io.element.android.libraries.architecture.Async
import io.element.android.libraries.matrix.ui.components.aMatrixUser
import kotlinx.collections.immutable.persistentListOf

open class CreateRoomRootStateProvider : PreviewParameterProvider<CreateRoomRootState> {
    override val values: Sequence<CreateRoomRootState>
        get() = sequenceOf(
            aCreateRoomRootState(),
            aCreateRoomRootState().copy(
                startDmAction = Async.Loading(),
                userListState = aMatrixUser().let {
                    aUserListState().copy(
                        searchQuery = it.id.value,
                        searchResults = persistentListOf(it),
                        selectedUsers = persistentListOf(it),
                        isSearchActive = true,
                    )
                }
            ),
            aCreateRoomRootState().copy(
                startDmAction = Async.Failure(Throwable()),
                userListState = aMatrixUser().let {
                    aUserListState().copy(
                        searchQuery = it.id.value,
                        searchResults = persistentListOf(it),
                        selectedUsers = persistentListOf(it),
                        isSearchActive = true,
                    )
                }
            ),
        )
}

fun aCreateRoomRootState() = CreateRoomRootState(
    eventSink = {},
    startDmAction = Async.Uninitialized,
    userListState = aUserListState(),
)
