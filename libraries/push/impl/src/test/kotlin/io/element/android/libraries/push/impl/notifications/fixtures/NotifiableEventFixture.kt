/*
 * Copyright (c) 2021 New Vector Ltd
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

package io.element.android.libraries.push.impl.notifications.fixtures

import io.element.android.libraries.matrix.api.core.EventId
import io.element.android.libraries.matrix.api.core.RoomId
import io.element.android.libraries.matrix.api.core.SessionId
import io.element.android.libraries.matrix.api.core.ThreadId
import io.element.android.libraries.matrix.test.AN_EVENT_ID
import io.element.android.libraries.matrix.test.A_ROOM_ID
import io.element.android.libraries.matrix.test.A_SESSION_ID
import io.element.android.libraries.push.impl.notifications.model.InviteNotifiableEvent
import io.element.android.libraries.push.impl.notifications.model.NotifiableMessageEvent
import io.element.android.libraries.push.impl.notifications.model.SimpleNotifiableEvent

fun aSimpleNotifiableEvent(
    sessionId: SessionId = A_SESSION_ID,
    roomId: RoomId = A_ROOM_ID,
    eventId: EventId = AN_EVENT_ID,
    type: String? = null,
    isRedacted: Boolean = false,
    canBeReplaced: Boolean = false,
    editedEventId: EventId? = null
) = SimpleNotifiableEvent(
    sessionId = sessionId,
    roomId = roomId,
    eventId = eventId,
    editedEventId = editedEventId,
    noisy = false,
    title = "title",
    description = "description",
    type = type,
    timestamp = 0,
    soundName = null,
    canBeReplaced = canBeReplaced,
    isRedacted = isRedacted
)

fun anInviteNotifiableEvent(
    sessionId: SessionId = A_SESSION_ID,
    roomId: RoomId = A_ROOM_ID,
    eventId: EventId = AN_EVENT_ID,
    isRedacted: Boolean = false
) = InviteNotifiableEvent(
    sessionId = sessionId,
    eventId = eventId,
    roomId = roomId,
    roomName = "a room name",
    editedEventId = null,
    noisy = false,
    title = "title",
    description = "description",
    type = null,
    timestamp = 0,
    soundName = null,
    canBeReplaced = false,
    isRedacted = isRedacted
)

fun aNotifiableMessageEvent(
    sessionId: SessionId = A_SESSION_ID,
    roomId: RoomId = A_ROOM_ID,
    eventId: EventId = AN_EVENT_ID,
    threadId: ThreadId? = null,
    isRedacted: Boolean = false
) = NotifiableMessageEvent(
    sessionId = sessionId,
    eventId = eventId,
    editedEventId = null,
    noisy = false,
    timestamp = 0,
    senderName = "sender-name",
    senderId = "sending-id",
    body = "message-body",
    roomId = roomId,
    threadId = threadId,
    roomName = "room-name",
    roomIsDirect = false,
    canBeReplaced = false,
    isRedacted = isRedacted,
    imageUriString = null
)
