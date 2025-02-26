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

package io.element.android.features.userlist.api.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.element.android.libraries.designsystem.components.avatar.AvatarSize
import io.element.android.libraries.designsystem.preview.ElementPreviewDark
import io.element.android.libraries.designsystem.preview.ElementPreviewLight
import io.element.android.libraries.matrix.ui.components.CheckableMatrixUserRow
import io.element.android.libraries.matrix.ui.components.aMatrixUser
import io.element.android.libraries.matrix.ui.model.MatrixUser

@Composable
fun SearchMultipleUsersResultItem(
    matrixUser: MatrixUser,
    isUserSelected: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    CheckableMatrixUserRow(
        checked = isUserSelected,
        modifier = modifier,
        matrixUser = matrixUser,
        avatarSize = AvatarSize.Custom(36.dp),
        onCheckedChange = onCheckedChange,
    )
}

@Preview
@Composable
internal fun SearchMultipleUsersResultItemLightPreview() = ElementPreviewLight { ContentToPreview() }

@Preview
@Composable
internal fun SearchMultipleUsersResultItemDarkPreview() = ElementPreviewDark { ContentToPreview() }

@Composable
private fun ContentToPreview() {
    Column {
        SearchMultipleUsersResultItem(matrixUser = aMatrixUser(), isUserSelected = true)
        SearchMultipleUsersResultItem(matrixUser = aMatrixUser(), isUserSelected = false)
    }
}
