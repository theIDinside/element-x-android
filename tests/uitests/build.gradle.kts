/*
 * Copyright (c) 2022 New Vector Ltd
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

import extension.allFeaturesImpl
import extension.allLibrariesImpl

plugins {
    id("io.element.android-compose-library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "io.element.android.tests.uitests"
}

dependencies {
    testImplementation(libs.test.junit)
    testImplementation(libs.test.parameter.injector)
    androidTestImplementation(libs.test.junitext)
    ksp(libs.showkase.processor)
    kspTest(libs.showkase.processor)

    implementation(libs.showkase)
    ksp(libs.showkase.processor)

    allLibrariesImpl()
    allFeaturesImpl(rootDir)
}
