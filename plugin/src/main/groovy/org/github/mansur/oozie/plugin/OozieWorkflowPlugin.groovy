package org.github.mansur.oozie.plugin

import org.github.mansur.oozie.extensions.OozieWorkflowExtension
import org.github.mansur.oozie.tasks.OozieWorkflowTask
import org.github.mansur.oozie.beans.*
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author Muhammad Ashraf
 * @since 7/27/13
 */
class OozieWorkflowPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'oozie'

    @Override
    void apply(Project project) {
      project.extensions.create(EXTENSION_NAME, OozieWorkflowExtension)
      addTask(project)
    }

    private void addTask(Project project) {
        project.tasks.withType(OozieWorkflowTask).whenTaskAdded { OozieWorkflowTask task ->
            def ext = project.extensions.findByName(EXTENSION_NAME)
            task.extension = ext
        }
    }
}
