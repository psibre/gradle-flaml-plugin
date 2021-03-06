package org.m2ci.msp.flaml

import groovy.transform.ToString
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty

@ToString(includeNames = true)
class FlamlExtension {

    Project project
    RegularFileProperty flacFile
    RegularFileProperty yamlFile

    FlamlExtension(Project project) {
        this.project = project
        this.flacFile = project.objects.fileProperty()
        this.yamlFile = project.objects.fileProperty()
    }

    void setFlacFile(File flacFile) {
        this.flacFile.set(flacFile)
    }

    void setFlacFile(String flacFilePath) {
        setFlacFile(project.file(flacFilePath))
    }

    void setYamlFile(File yamlFile) {
        this.yamlFile.set(yamlFile)
    }

    void setYamlFile(String yamlFilePath) {
        setYamlFile(project.file(yamlFilePath))
    }
}
