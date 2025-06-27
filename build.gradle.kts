import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.arturbosch.detekt) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.maven.publish) apply false
}

val detektVersion = libs.versions.detekt.get()
subprojects {
    // formatting code for all subprojects
    apply<SpotlessPlugin>()
    configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("build/**/*.kt")
            ktlint()
            endWithNewline()
        }
        kotlinGradle {
            target("*.kts")
            ktlint()
        }
    }
    // code analysis for all subprojects
    apply<DetektPlugin>()
    configure<DetektExtension> {
        toolVersion = detektVersion
        config.from("$rootDir/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        reports {
            xml.required.set(false)
            html.required.set(true)
            txt.required.set(false)
        }
    }

    afterEvaluate {
        tasks.withType<KotlinCompile> {
            finalizedBy("spotlessApply")
        }

        tasks.withType<KotlinCompile> {
            finalizedBy("detekt")
        }

    }
}