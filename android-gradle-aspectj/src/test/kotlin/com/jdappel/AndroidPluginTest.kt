package com.jdappel

import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * TODO: Add description
 *
 * @author archinamon on 11/04/17.
 */
class AndroidPluginTest {

    @Test
    fun detectAppPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.android.application")))
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj")))
    }

    @Test
    fun detectAppExtPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.android.application")))
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj-ext")))
    }

    @Test
    fun detectTestPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.android.application")))
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj-test")))
    }

    @Test
    fun detectLibPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.android.library")))
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj")))
    }

    @Test
    fun detectLibExtPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.android.library")))
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj-ext")))
    }

    @Test
    fun detectLibTestPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.android.library")))
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj-test")))
    }

    @Test
    fun detectLibProvidesPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.android.library")))
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj-provides")))
    }

    @Test(expected = GradleException::class)
    fun failsWithoutAndroidPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj")))
    }

    @Test(expected = GradleException::class)
    fun failsExtWithoutAndroidPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj-ext")))
    }

    @Test(expected = GradleException::class)
    fun failsTestWithoutAndroidPlugin() {
        val project = ProjectBuilder.builder().build()
        project.apply(mapOf(Pair("plugin", "com.jdappel.aspectj-test")))
    }
}