import org.apache.tools.ant.taskdefs.condition.Os
import org.apache.tools.ant.taskdefs.condition.Os.FAMILY_WINDOWS
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.maps.secrets)
}

if (!file("secrets.properties").exists()) {
    exec {
        if (Os.isFamily(FAMILY_WINDOWS)) {
            commandLine("cmd", "/c", "copy secrets.properties.example secrets.properties")
        } else {
            commandLine("sh", "-c", "cp secrets.properties.example secrets.properties")
        }
    }
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.andreformosa.nearbyplaces"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //  Makes the Android Test Orchestrator run its "pm clear" command after each test invocation.
        //  This command ensures that the app's state is completely cleared between tests.
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        buildConfigField(
            "String",
            "FOURSQUARE_BASE_URL",
            "\"https://api.foursquare.com/v3/\""
        )

        val secretProperties = Properties()
        secretProperties.load(file("secrets.properties").inputStream())
        buildConfigField(
            "String",
            "FOURSQUARE_API_KEY",
            "\"${secretProperties.getProperty("FOURSQUARE_API_KEY") ?: ""}\""
        )
    }
    signingConfigs {
        val secretProperties = Properties()
        secretProperties.load(file("secrets.properties").inputStream())

        getByName("debug") {
            keyAlias = "debugkeystore"
            keyPassword = secretProperties.getProperty("DEBUG_KEYSTORE_PWD")
            storeFile = file("../keystore/debug_keystore.jks")
            storePassword = secretProperties.getProperty("DEBUG_KEYSTORE_PWD")
        }
        create("release") {
            keyAlias = "releasekeystore"
            keyPassword = secretProperties.getProperty("RELEASE_KEYSTORE_PWD")
            storeFile = file("../keystore/release_keystore.jks")
            storePassword = secretProperties.getProperty("RELEASE_KEYSTORE_PWD")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

secrets {
    propertiesFileName = "maps-secrets.properties"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.splash.screen)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.maps.compose)
    implementation(libs.material3)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.sandwich)
    implementation(libs.timber)

    debugImplementation(libs.chucker)
    debugImplementation(libs.androidx.compose.ui.tooling)

    releaseImplementation(libs.chucker.noOp)

    // Test dependencies
    testImplementation(libs.jUnit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)

    // Compose testing dependencies
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.uiautomator)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestUtil(libs.androidx.test.orchestrator)
}
