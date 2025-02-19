/*
 * This file is part of Toxopid, a Gradle plugin for Mindustry mods/plugins.
 *
 * MIT License
 *
 * Copyright (c) 2022 Xpdustry
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.xpdustry.toxopid.task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * This task downloads a list of artifacts from GitHub.
 */
@Deprecated(
    message = "This class has been replaced by a far simpler alternative.",
    replaceWith = ReplaceWith("fr.xpdustry.toxopid.task.ModArtifactDownload")
)
@CacheableTask
open class GitHubDownload : DefaultTask() {

    @get:Input
    val artifacts: ListProperty<GitHubArtifact> =
        project.objects.listProperty(GitHubArtifact::class.java)

    @get:OutputFiles
    val files: List<File>
        get() = artifacts.get().map { temporaryDir.resolve(it.name) }

    @TaskAction
    fun downloadArtifacts() {
        project.delete(temporaryDir.listFiles())
        artifacts.get().forEach { artifact ->
            val file = temporaryDir.resolve(artifact.name)
            artifact.url.openStream().use { `in` -> file.outputStream().use { `in`.copyTo(it) } }
        }
    }
}
