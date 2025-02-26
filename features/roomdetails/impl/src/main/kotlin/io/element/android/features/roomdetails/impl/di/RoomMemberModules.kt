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

package io.element.android.features.roomdetails.impl.di

import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.element.android.features.roomdetails.impl.members.RoomUserListDataSource
import io.element.android.features.roomdetails.impl.members.details.RoomMemberDetailsPresenter
import io.element.android.features.userlist.api.UserListDataSource
import io.element.android.libraries.di.RoomScope
import io.element.android.libraries.matrix.api.MatrixClient
import io.element.android.libraries.matrix.api.room.MatrixRoom
import io.element.android.libraries.matrix.api.room.RoomMember
import javax.inject.Named

@Module
@ContributesTo(RoomScope::class)
interface RoomMemberBindsModule {

    @Binds
    @Named("RoomMembers")
    fun bindRoomMemberUserListDataSource(dataSource: RoomUserListDataSource): UserListDataSource
}

@Module
@ContributesTo(RoomScope::class)
object RoomMemberProvidesModule {

    @Provides
    fun provideRoomMemberDetailsPresenterFactory(
        matrixClient: MatrixClient,
        room: MatrixRoom,
    ): RoomMemberDetailsPresenter.Factory {
        return object : RoomMemberDetailsPresenter.Factory {
            override fun create(roomMember: RoomMember): RoomMemberDetailsPresenter {
                return RoomMemberDetailsPresenter(matrixClient.sessionId, room, roomMember)
            }
        }
    }
}
