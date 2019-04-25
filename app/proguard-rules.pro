# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/damien/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include ic_customer_offer and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class last_name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn com.squareup.okhttp.**

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

###### dagger 2.0 stuff
-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keepnames class com.ourcompany.**

-keep class **$$ModuleAdapter { *; }
-keepnames class **$$InjectAdapter { *; }

-keepclassmembers class * {
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <init>(...);
}
-adaptclassstrings

-keep class org.ocpsoft.prettytime.i18n.**
