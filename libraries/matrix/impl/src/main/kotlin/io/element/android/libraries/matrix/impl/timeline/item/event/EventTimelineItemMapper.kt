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

package io.element.android.libraries.matrix.impl.timeline.item.event

import io.element.android.libraries.matrix.api.core.EventId
import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.matrix.api.timeline.item.event.EventReaction
import io.element.android.libraries.matrix.api.timeline.item.event.EventSendState
import io.element.android.libraries.matrix.api.timeline.item.event.EventTimelineItem
import io.element.android.libraries.matrix.api.timeline.item.event.ProfileTimelineDetails
import org.matrix.rustcomponents.sdk.Reaction
import org.matrix.rustcomponents.sdk.EventSendState as RustEventSendState
import org.matrix.rustcomponents.sdk.EventTimelineItem as RustEventTimelineItem
import org.matrix.rustcomponents.sdk.ProfileDetails as RustProfileDetails

class EventTimelineItemMapper(private val contentMapper: TimelineEventContentMapper = TimelineEventContentMapper()) {

    fun map(eventTimelineItem: RustEventTimelineItem): EventTimelineItem = eventTimelineItem.use {
        EventTimelineItem(
            uniqueIdentifier = eventTimelineItem.uniqueIdentifier(),
            eventId = eventTimelineItem.eventId()?.let { EventId(it) },
            isEditable = eventTimelineItem.isEditable(),
            isLocal = eventTimelineItem.isLocal(),
            isOwn = eventTimelineItem.isOwn(),
            isRemote = eventTimelineItem.isRemote(),
            localSendState = eventTimelineItem.localSendState()?.map(),
            reactions = eventTimelineItem.reactions().map(),
            sender = UserId(eventTimelineItem.sender()),
            senderProfile = eventTimelineItem.senderProfile().map(),
            timestamp = eventTimelineItem.timestamp().toLong(),
            content = contentMapper.map(eventTimelineItem.content())
        )
    }
}

fun RustProfileDetails.map(): ProfileTimelineDetails {
    return when (this) {
        RustProfileDetails.Pending -> ProfileTimelineDetails.Pending
        RustProfileDetails.Unavailable -> ProfileTimelineDetails.Unavailable
        is RustProfileDetails.Error -> ProfileTimelineDetails.Error(message)
        is RustProfileDetails.Ready -> ProfileTimelineDetails.Ready(
            displayName = displayName,
            displayNameAmbiguous = displayNameAmbiguous,
            avatarUrl = avatarUrl
        )
    }
}

fun RustEventSendState?.map(): EventSendState? {
    return when (this) {
        null -> null
        RustEventSendState.NotSendYet -> EventSendState.NotSendYet
        is RustEventSendState.SendingFailed -> EventSendState.SendingFailed(error)
        is RustEventSendState.Sent -> EventSendState.Sent(EventId(eventId))
    }
}

private fun List<Reaction>?.map(): List<EventReaction> {
    return this?.map {
        EventReaction(
            key = it.key,
            count = it.count.toLong()
        )
    } ?: emptyList()
}
