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

package io.element.android.libraries.matrix.impl.room

import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.matrix.api.room.RoomMember
import io.element.android.libraries.matrix.api.room.RoomMembershipState
import org.matrix.rustcomponents.sdk.MembershipState as RustMembershipState
import org.matrix.rustcomponents.sdk.RoomMember as RustRoomMember

object RoomMemberMapper {

    fun map(roomMember: RustRoomMember): RoomMember =
        RoomMember(
            UserId(roomMember.userId()),
            roomMember.displayName(),
            roomMember.avatarUrl(),
            mapMembership(roomMember.membership()),
            roomMember.isNameAmbiguous(),
            roomMember.powerLevel(),
            roomMember.normalizedPowerLevel(),
            roomMember.isIgnored(),
        )

    fun mapMembership(membershipState: RustMembershipState): RoomMembershipState =
        when (membershipState) {
            RustMembershipState.BAN -> RoomMembershipState.BAN
            RustMembershipState.INVITE -> RoomMembershipState.INVITE
            RustMembershipState.JOIN -> RoomMembershipState.JOIN
            RustMembershipState.KNOCK -> RoomMembershipState.KNOCK
            RustMembershipState.LEAVE -> RoomMembershipState.LEAVE
        }
}
