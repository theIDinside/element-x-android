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

package io.element.android.features.roomdetails.impl.members

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.plugin.Plugin
import com.bumble.appyx.core.plugin.plugins
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.element.android.anvilannotations.ContributesNode
import io.element.android.libraries.di.RoomScope
import io.element.android.libraries.matrix.api.room.MatrixRoom
import io.element.android.libraries.matrix.api.room.RoomMember
import io.element.android.libraries.matrix.ui.model.MatrixUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesNode(RoomScope::class)
class RoomMemberListNode @AssistedInject constructor(
    @Assisted buildContext: BuildContext,
    @Assisted plugins: List<Plugin>,
    private val room: MatrixRoom,
    private val presenter: RoomMemberListPresenter,
    private val coroutineScope: CoroutineScope,
) : Node(buildContext, plugins = plugins) {

    interface Callback : Plugin {
        fun openRoomMemberDetails(roomMember: RoomMember)
    }

    private val callbacks = plugins<Callback>()

    private fun onUserSelected(matrixUser: MatrixUser) = coroutineScope.launch {
        val member = room.getMember(matrixUser.id).firstOrNull()
        if (member != null) {
            callbacks.forEach { it.openRoomMemberDetails(member) }
        } else {
            Timber.e("Could find room member ${matrixUser.id} in room ${room.roomId}")
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        val state = presenter.present()
        RoomMemberListView(
            state = state,
            modifier = modifier,
            onBackPressed = { navigateUp() },
            onUserSelected = ::onUserSelected,
        )
    }
}
