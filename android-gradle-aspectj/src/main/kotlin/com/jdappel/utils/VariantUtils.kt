package com.jdappel.utils

import com.android.build.gradle.BasePlugin
import com.android.build.gradle.internal.api.dsl.extensions.BaseExtension2
import com.android.build.gradle.internal.scope.VariantScope
import com.android.build.gradle.internal.variant.BaseVariantData
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.compile.JavaCompile
import java.io.File

fun getJavaTask(baseVariantData: BaseVariantData): JavaCompile? {
    return baseVariantData.taskContainer.javacTask.get()
}

fun getAjSourceAndExcludeFromJavac(project: Project, variantData: BaseVariantData): FileCollection {
    val javaTask = getJavaTask(variantData)

    val flavors: List<String>? = variantData.variantConfiguration.productFlavors.map { flavor -> flavor.name }
    val srcSet = mutableListOf("main", variantData.variantConfiguration!!.buildType!!.name)
    flavors?.let { srcSet.addAll(it) }

    val srcDirs = srcSet.map { "src/$it/aspectj" }
    val aspects: FileCollection = project.layout.files(srcDirs.map { project.file(it) })

    javaTask!!.exclude { treeElem ->
        treeElem.file in aspects.files
    }

    return aspects.filter(File::exists)
}

fun findAjSourcesForVariant(project: Project, variantName: String): MutableSet<File> {
    val possibleDirs: MutableSet<File> = mutableSetOf()
    if (project.file("src/main/aspectj").exists()) {
        possibleDirs.add(project.file("src/main/aspectj"))
    }

    val types = variantName.split("(?=\\p{Upper})".toRegex())
    val root = project.file("src").listFiles()

    root.forEach { file ->
        types.forEach { type ->
            if (file.name.contains(type.toLowerCase()) &&
                    file.list().any { it.contains("aspectj") }) {
                possibleDirs.add(File(file, "aspectj"))
            }
        }
    }

    return LinkedHashSet(possibleDirs)
}

fun getVariantDataList(plugin: BasePlugin<out BaseExtension2>): List<BaseVariantData> {
    return plugin.variantManager.variantScopes.map(VariantScope::getVariantData)
}

internal infix fun <E> MutableCollection<in E>.shl(elem: E): MutableCollection<in E> {
    this.add(elem)
    return this
}

internal infix fun <E> MutableCollection<in E>.from(elems: Collection<E>) {
    this.addAll(elems)
}