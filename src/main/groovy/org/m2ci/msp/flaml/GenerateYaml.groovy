package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class GenerateYaml extends DefaultTask {

    @InputFiles
    FileCollection srcFiles = project.files()

    @OutputFile
    final RegularFileProperty yamlFile = newOutputFile()

    @TaskAction
    void generate() {
        def prompts = []
        def start = 0.0
        srcFiles.each { srcFile ->
            // determine end time from WAV via soxi
            def soxi = new ByteArrayOutputStream()
            project.exec {
                commandLine 'soxi', '-r', srcFile
                standardOutput = soxi
            }
            def sampleRate = soxi.toString().readLines().first() as BigDecimal
            soxi = new ByteArrayOutputStream()
            project.exec {
                commandLine 'soxi', '-s', srcFile
                standardOutput = soxi
            }
            def samples = soxi.toString().readLines().first() as BigDecimal
            def end = start + samples / sampleRate

            // add prompt
            prompts << [
                    prompt: srcFile.name - '.wav',
                    start : start,
                    end   : end
            ]
            start = end
        }
        def options = new DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        def yaml = new Yaml(options)
        yaml.dump(prompts, yamlFile.get().asFile.newWriter('UTF-8'))
    }
}