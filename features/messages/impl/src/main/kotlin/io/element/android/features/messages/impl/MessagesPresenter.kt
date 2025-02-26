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

package io.element.android.features.messages.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import io.element.android.features.messages.impl.actionlist.ActionListPresenter
import io.element.android.features.messages.impl.actionlist.model.TimelineItemAction
import io.element.android.features.messages.impl.textcomposer.MessageComposerEvents
import io.element.android.features.messages.impl.textcomposer.MessageComposerPresenter
import io.element.android.features.messages.impl.textcomposer.MessageComposerState
import io.element.android.features.messages.impl.timeline.TimelineEvents
import io.element.android.features.messages.impl.timeline.TimelinePresenter
import io.element.android.features.messages.impl.timeline.model.TimelineItem
import io.element.android.features.messages.impl.timeline.model.event.TimelineItemTextBasedContent
import io.element.android.libraries.architecture.Presenter
import io.element.android.libraries.designsystem.components.avatar.AvatarData
import io.element.android.libraries.designsystem.components.avatar.AvatarSize
import io.element.android.libraries.matrix.api.room.MatrixRoom
import io.element.android.libraries.textcomposer.MessageComposerMode
import io.element.android.features.networkmonitor.api.NetworkMonitor
import io.element.android.features.networkmonitor.api.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MessagesPresenter @Inject constructor(
    private val room: MatrixRoom,
    private val composerPresenter: MessageComposerPresenter,
    private val timelinePresenter: TimelinePresenter,
    private val actionListPresenter: ActionListPresenter,
    private val networkMonitor: NetworkMonitor,
) : Presenter<MessagesState> {

    @Composable
    override fun present(): MessagesState {
        val localCoroutineScope = rememberCoroutineScope()
        val composerState = composerPresenter.present()
        val timelineState = timelinePresenter.present()
        val actionListState = actionListPresenter.present()

        val syncUpdateFlow = room.syncUpdateFlow().collectAsState(0L)
        val roomName: MutableState<String?> = rememberSaveable {
            mutableStateOf(null)
        }
        val roomAvatar: MutableState<AvatarData?> = remember {
            mutableStateOf(null)
        }

        val networkConnectionStatus by networkMonitor.connectivity.collectAsState(initial = networkMonitor.currentConnectivityStatus)
        println(networkConnectionStatus)

        LaunchedEffect(syncUpdateFlow) {
            roomAvatar.value =
                AvatarData(
                    id = room.roomId.value,
                    name = room.name,
                    url = room.avatarUrl,
                    size = AvatarSize.SMALL
                )
            roomName.value = room.name
        }
        LaunchedEffect(composerState.mode.relatedEventId) {
            timelineState.eventSink(TimelineEvents.SetHighlightedEvent(composerState.mode.relatedEventId))
        }
        fun handleEvents(event: MessagesEvents) {
            when (event) {
                is MessagesEvents.HandleAction -> localCoroutineScope.handleTimelineAction(event.action, event.event, composerState)
            }
        }
        return MessagesState(
            roomId = room.roomId,
            roomName = roomName.value,
            roomAvatar = roomAvatar.value,
            composerState = composerState,
            timelineState = timelineState,
            actionListState = actionListState,
            hasNetworkConnection = networkConnectionStatus == NetworkStatus.Online,
            eventSink = ::handleEvents
        )
    }

    fun CoroutineScope.handleTimelineAction(
        action: TimelineItemAction,
        targetEvent: TimelineItem.Event,
        composerState: MessageComposerState,
    ) = launch {
        when (action) {
            TimelineItemAction.Copy -> notImplementedYet()
            TimelineItemAction.Forward -> notImplementedYet()
            TimelineItemAction.Redact -> handleActionRedact(targetEvent)
            TimelineItemAction.Edit -> handleActionEdit(targetEvent, composerState)
            TimelineItemAction.Reply -> handleActionReply(targetEvent, composerState)
        }
    }

    private fun notImplementedYet() {
        Timber.v("NotImplementedYet")
    }

    private suspend fun handleActionRedact(event: TimelineItem.Event) {
        if (event.eventId == null) return
        room.redactEvent(event.eventId)
    }

    private fun handleActionEdit(targetEvent: TimelineItem.Event, composerState: MessageComposerState) {
        if (targetEvent.eventId == null) return
        val composerMode = MessageComposerMode.Edit(
            targetEvent.eventId,
            (targetEvent.content as? TimelineItemTextBasedContent)?.body.orEmpty()
        )
        composerState.eventSink(
            MessageComposerEvents.SetMode(composerMode)
        )
    }

    private fun handleActionReply(targetEvent: TimelineItem.Event, composerState: MessageComposerState) {
        if (targetEvent.eventId == null) return
        val composerMode = MessageComposerMode.Reply(targetEvent.safeSenderName, targetEvent.eventId, "")
        composerState.eventSink(
            MessageComposerEvents.SetMode(composerMode)
        )
    }
}
