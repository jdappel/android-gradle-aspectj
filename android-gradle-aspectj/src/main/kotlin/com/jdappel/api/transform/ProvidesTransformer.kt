package com.jdappel.api.transform

import com.android.build.api.transform.TransformInvocation
import com.jdappel.utils.logBypassTransformation
import org.gradle.api.Project

internal class ProvidesTransformer(project: Project): AspectJTransform(project, BuildPolicy.SIMPLE) {

    override fun prepareProject(): AspectJTransform {
        return this
    }

    override fun transform(transformInvocation: TransformInvocation) {
        logBypassTransformation()
    }
}