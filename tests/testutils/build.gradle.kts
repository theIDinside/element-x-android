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
plugins {
    id("io.element.android-library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.element.android.tests.testutils"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.test.junit)
    implementation(libs.test.mockk)
    implementation(libs.test.truth)
    implementation(libs.test.turbine)
    implementation(libs.coroutines.test)
    implementation(projects.libraries.matrix.test)
    implementation(projects.services.appnavstate.test)
}
