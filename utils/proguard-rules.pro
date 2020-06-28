# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keepnames class kotlinx.** { *; }


-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field
-ignorewarnings

#
# For Kotlin reflection
#

-keep class kotlin.reflect.jvm.internal.impl.** { *; }
-keep interface com.merseyside.utils.reflection.ReflectionHelper

-keep interface kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoader

-keep class kotlin.Metadata { *; }

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-keep class com.google.api.client.googleapis.** { *; }
-keep class com.google.api.services.** { *; }