// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.8.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
}
val defaultMinSdkVersion by extra(33)
val defaultTargetSdkVersion by extra(34)
