# GradleAspectJ-Android
[![Kotlin](https://img.shields.io/badge/Kotlin-1.2.71-blue.svg)](http://kotlinlang.org) ![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg) [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/jdappel/maven/android-gradle-aspectj/images/download.svg) ](https://bintray.com/archinamon/maven/android-gradle-aspectj/_latestVersion)

A Gradle plugin which enables AspectJ for Android builds.
Supports writing code with AspectJ-lang in `.aj` files and in java-annotation style.
Full support of Android product flavors and build types.
Support Kotlin, Groovy, Scala and any other languages that compiles into java bytecode.

Actual version: `com.jdappel:android-gradle-aspectj:3.3.0`.
<br />
Friendly with <a href="https://zeroturnaround.com/software/jrebel-for-android/" target="_blank">jRebel for Android</a>!

This plugin is completely friendly with <a href="https://bitbucket.org/hvisser/android-apt" target="_blank">APT</a> (Android Annotation Processing Tools) and <a href="https://github.com/evant/gradle-retrolambda/" target="_blank">Retrolambda</a> project (but Java 8 not supported in .aj files).
<a href="https://github.com/excilys/androidannotations" target="_blank">AndroidAnnotations</a>, <a href="https://github.com/square/dagger" target="_blank">Dagger</a> are also supported and works fine.

This plugin has many ideas from the others similar projects, but no one of them grants full pack of features like this one.
Nowdays it has been completely re-written using Transform API.

Key features
-----

Augments Java, Kotlin, Groovy bytecode simultaneously!<br />
Works with background mechanics of jvm-based languages out-of-box!<br />
[How to teach Android Studio to understand the AspectJ!](IDE)<br />
Works properly with AS 3.2.0 and AGP 3.2.0

It is easy to isolate your code with aspect classes, that will be simply injected via cross-point functions, named `advices`, into your core application. The main idea is — code less, do more!

AspectJ-Gradle plugin provides supply of all known JVM-based languages, such as Groovy, Kotlin, etc. That means you can easily write cool stuff which may be inject into any JVM language, not only Java itself! :)

Two simple rules you may consider when writing aspect classes.
- Do not write aspects outside the `src/$flavor/aspectj` source set! These aj-classes will be excluded from java compiler.
- Do not try to access aspect classes from java/kotlin/etc. In case java compiler doesn't know anything about aspectj, it will lead to compile errors on javac step.

These rules affects only in case you're writing in native aj-syntax.
You may write aspects in java-annotation style and being free from these limitations.

Usage
-----

First add a maven repo link into your `repositories` block of module build file:
```groovy
mavenCentral()
```
Don't forget to add `mavenCentral()` due to some dependencies inside AspectJ-gradle module.

Add the plugin to your `buildscript`'s `dependencies` section:
```groovy
classpath 'com.jdappel:android-gradle-aspectj:3.3.0'
```

Apply the `aspectj` plugin:
```groovy
apply plugin: 'com.jdappel.aspectj'
```

Now you can write aspects using annotation style or native (even without IntelliJ IDEA Ultimate edition).
Let's write simple Application advice:
```java
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

aspect AppStartNotifier {

    pointcut postInit(): within(Application+) && execution(* Application+.onCreate());

    after() returning: postInit() {
        Application app = (Application) thisJoinPoint.getTarget();
        NotificationManager nmng = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
        nmng.notify(9999, new NotificationCompat.Builder(app)
            .setTicker("Hello AspectJ")
            .setContentTitle("Notification from aspectJ")
            .setContentText("privileged aspect AppAdvice")
            .setSmallIcon(R.drawable.ic_launcher)
            .build());
    }
}
```

Tune extension
-------

```groovy
aspectj {
    ajc '1.8.12' // default value

    /* @see Ext plugin config **/
    includeAllJars false // default value
    includeJar 'design', 'support-v4', 'dagger' // default is empty
    excludeJar 'support-v7', 'joda' // default is empty
    extendClasspath true // default value

    includeAspectsFromJar 'my-aj-logger-lib', 'any-other-libs-with-aspects'  // default is empty
    ajcArgs << '-referenceInfo' << '-warn:deprecation'

    weaveInfo true // default value
    debugInfo false // default value
    addSerialVersionUID false // default value
    noInlineAround false // default value
    ignoreErrors false // default value
    
    breakOnError true // default value
    experimental false // default value
    buildTimeLog true // default value

    transformLogFile 'ajc-transform.log' // default value
    compilationLogFile 'ajc-compile.log' // default value
}
```
Note that you may not include all these options!

All the extension parameters are have default values (all of them are described above, except of includeJar/Aspects/ajcArgs options).
So no need to define them manually.

- `ajc` Allows to define the aspectj runtime jar version manually (1.8.12 current)
- `extendClasspath` Explicitly controls whether plugin should mutate the classpath with aspectj-runtime itself

- `includeAllJars` Explicitly include all available jar-files into -inpath to proceed by AJ-compiler
- `includeJar` Name filter to include any jar/aar which name or path satisfies the filter
- `excludeJar` Name filter to exclude any jar/aar which name or path satisfies the filter
- `includeAspectsFromJar` Name filter to include any jar/aar with compiled binary aspects you wanna affect your project
- `ajcExtraArgs` Additional parameters for aspectj compiler

- `weaveInfo` Enables printing info messages from Aj compiler
- `debugInfo` Adds special debug info in aspect's bytecode
- `addSerialVersionUID` Adds serialVersionUID field for Serializable-implemented aspect classes
- `noInlineAround` Strict ajc to inline around advice's body into the target methods
- `ignoreErrors` Prevent compiler from aborting if errors occurrs during processing the sources

- `breakOnError` Allows to continue project building when ajc fails or throws any errors
- `experimental` Enables experimental ajc options: `-XhasMember` and `-Xjoinpoints:synchronization,arrayconstruction`.
- `buildTimeLog` Appends a BuildTimeListener to current module that prints time spent for every task in build flow, granularity in millis

- `transformLogFile` Defines name for the log file where all Aj compiler info writes to, new separated for Transformer
- `compilationLogFile` Defines name for the log file where all Aj compiler info writes to, new separated for CompileTask

Extended plugin config
-----------------
```groovy
apply plugin: 'com.jdappel.aspectj-ext'
```

Ext config:
- allows usage of `includeJar` and `includeAllJars` parameters, with workaround to avoid `Multiple dex files exception`
- supports `multiDex`
- supports `Instrumented tests`

Currently it has some limitations:
- `InstantRun` must be switched off (Plugin detects IR status and fails build if IR will be found).

Provider plugin config
-----------------
```groovy
apply plugin: 'com.jdappel.aspectj-provides'
```

Plugin-provider may be useful for that cases when you need to extract aspect-sources into separate module and include it on demand to that modules where you only need it.
Therefor this behavior will save you build-time due to bypassing aspectj-transformers in provide-only modules.

You ain't limited to describe as much provider-modules as you need and then include them using `includeAspectsFromJar` parameter in the module which code or dependencies you may want to augment.

Working tests
-------
```groovy
apply plugin: 'com.jdappel.aspectj-test'
```

Test scope configuration inherits `aspectj-ext` behavior with strictly excluding compile and transform tasks from non-test build variant.
In other words only instrumentation `androidTest` will work with this sub-plugin.
In case unit tests doesn't really have any specials (excluding source/target code version base) so `aspectj-test` scope won't affect `unitTest` variants.

ProGuard
-------
Correct tuning will depends on your own usage of aspect classes. So if you declares inter-type injections you'll have to predict side-effects and define your annotations/interfaces which you inject into java classes/methods/etc. in proguard config.

Basic rules you'll need to declare for your project:
```
-adaptclassstrings
-keepattributes InnerClasses, EnclosingMethod, Signature, *Annotation*

-keepnames @org.aspectj.lang.annotation.Aspect class * {
    ajc* <methods>;
}
```

If you will face problems with lambda factories, you may need to explicitely suppress them. That could happen not in aspect classes but in any arbitrary java-class if you're using Retrolambda.
So concrete rule is:
```
-keep class *$Lambda* { <methods>; }
-keepclassmembernames public class * {
    *** lambda*(...);
}
```

Changelog
---------
#### 3.3.0 -- AGP 3.2.0 and Gradle 4.10.2 Support
* added support for version 3.2.0 of the Android Gradle plugin

All these limits are fighting on and I'll be glad to introduce new build as soon as I solve these problems.

License
-------

    Copyright 2018 Jeremy Appel.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
