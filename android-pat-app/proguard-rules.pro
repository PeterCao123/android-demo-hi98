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

# Global
-verbose
-ignorewarnings
-optimizationpasses 5
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*

# PAY@TABLE
-keepclassmembers class com.shiji.png.pat.spat.dto.** {
  <fields>;
}
# spat-client
-keepclassmembers class com.shiji.png.pat.spat.model.** {
  <fields>;
}
# pat-model
-keepclassmembers class com.shiji.png.pat.model.* {
  <fields>;
}
# payment-api
-keep @com.shiji.png.droid.payment.annotation.ServiceDef class *
-keep enum com.shiji.png.droid.payment.annotation.ServiceType { *; }
-keep public class * extends com.shiji.png.droid.payment.ServiceFactory
-keep public class com.shiji.png.droid.payment.message.** {*;}
# payment-icbc-macau
-keepclassmembers class com.shiji.png.droid.macau.model.VasRequestBody {*;}
-keep public class com.arke.vas.**{*;}

# rxjava

# rxandroid

# slf4j

# logback-android

# retrofit2
-keepattributes Signature, InnerClasses
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**

# okhttp3
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider
