import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}
val versionName = project.findProperty("VERSION_NAME") as String? ?: "0.0.6-alpha"
android {
    namespace = "com.nareshchocha.formz"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}

dependencies {

    testImplementation(libs.junit)
}

val automaticRelease: Boolean = true
mavenPublishing {
    publishToMavenCentral(automaticRelease)
    signAllPublications()
    coordinates("io.github.chochanaresh", "formz", versionName)

    pom {
        name.set("formz")
        description.set(
            "Formz is a lightweight validation framework for Android forms written in Kotlin. It provides a simple, yet powerful way to define, validate, and manage form inputs in your Android applications. The library is designed with immutability and performance in mind, ensuring that expensive validation logic is computed only once per input."
        )
        inceptionYear.set("2025")
        url.set("https://github.com/ChochaNaresh/Formz")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("ChochaNaresh")
                name.set("Naresh Chocha")
                url.set("https://github.com/ChochaNaresh")
            }
        }
        scm {
            url.set("https://github.com/ChochaNaresh/Formz")
            connection.set("scm:git:git://github.com/ChochaNaresh/Formz.git")
            developerConnection.set("scm:git:ssh://git@github.com/ChochaNaresh/Formz.git")
        }
    }
}
