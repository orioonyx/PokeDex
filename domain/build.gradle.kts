plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.github.orioonyx.pokedex.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // modules
    implementation(project(Modules.core))
    testImplementation(project(Modules.core_test))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // test
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.assertions.core)

    // coroutines
    implementation(libs.coroutines)
    testImplementation(libs.coroutines)
    testImplementation(libs.coroutines.test)

    // json parsing
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // logger
    api(libs.timber)
}