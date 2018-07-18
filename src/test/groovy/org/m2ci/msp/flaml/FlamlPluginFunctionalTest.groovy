package org.m2ci.msp.flaml

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class FlamlPluginFunctionalTest {

    GradleRunner gradle

    @BeforeClass
    void setup() {
        def projectDir = File.createTempDir()
        gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir)
        ['build-core.gradle', 'build-extraction.gradle', 'foobar.flac', 'foobar.yaml', 'foobar.wav', 'foobar.TextGrid',
         'foo.wav', 'bar.wav', 'foo.lab', 'bar.lab', 'foo.txt', 'bar.txt'].each { resourceName ->
            new File(projectDir, resourceName).withOutputStream { stream ->
                stream << this.getClass().getResourceAsStream(resourceName)
            }
        }
    }

    @DataProvider
    Object[][] coreTasks() {
        [
                ['help'],
                ['hasPlugin'],
                ['tasks'],
                ['hasTaskDescriptions'],
                ['hasExtension'],
                ['hasFlamlResources'],
                ['hasTestResources'],
        ]
    }

    @DataProvider
    Object[][] extractionTasks() {
        [
                ['testExtractWavFiles'],
                ['testExtractLabFiles'],
                ['testExtractTextGrid'],
                ['testExtractTextFiles']
        ]
    }

    @Test(dataProvider = 'coreTasks')
    void testCoreTasks(String taskName) {
        def result = gradle.withArguments('--build-file', 'build-core.gradle', taskName).build()
        println result.output
        assert result.task(":$taskName").outcome in [TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE]
    }

    @Test(dataProvider = 'extractionTasks')
    void testExtractionTasks(String taskName) {
        def result = gradle.withArguments('--build-file', 'build-extraction.gradle', taskName).build()
        println result.output
        assert result.task(":$taskName").outcome in [TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE]
    }
}
