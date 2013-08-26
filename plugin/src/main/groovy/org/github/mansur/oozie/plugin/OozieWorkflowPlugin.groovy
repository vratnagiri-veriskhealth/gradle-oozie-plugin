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
      project.metaClass.oozieHive= { params -> new HiveNode(params) }
      project.metaClass.ooziePig= { params -> new PigNode(params) }
      project.metaClass.oozieJava= { params -> new JavaNode(params) }
      project.metaClass.oozieShell = { params -> new ShellNode(params) }
      project.metaClass.oozieSsh = { params -> new SshNode(params) }
      project.metaClass.oozieFs= { params -> new FsNode(params) }
      project.metaClass.oozieFsMove= { params -> new FsMoveNode(params) }
      project.metaClass.oozieFsChmod= { params -> new FsChmodNode(params) }
      project.metaClass.oozieEmail= { params -> new EmailNode(params) }
      project.metaClass.oozieKill= { params -> new KillNode(params) }
      project.metaClass.oozieCase= { to, condition -> new DecisionCaseNode([to: to, condition: condition]) }
      project.metaClass.oozieDecision= { params -> new DecisionNode(params) }
      project.metaClass.oozieFork= { params -> new ForkNode(params) }
      project.metaClass.oozieJoin= { params -> new JoinNode(params) }
    }

    private void addTask(Project project) {
        project.tasks.withType(OozieWorkflowTask).whenTaskAdded { OozieWorkflowTask task ->
            def ext = project.extensions.findByName(EXTENSION_NAME)
            task.conventionMapping.workflowActions = { ext.actions }
            task.conventionMapping.common = { ext.common == null ? [:] : ext.common }
            task.conventionMapping.end = { ext.end }
            task.conventionMapping.workflowName = { ext.name }
            task.conventionMapping.namespace = { ext.namespace }
            task.conventionMapping.jobXML = { ext.jobXML == null ? [:] : ext.jobXML }
            task.conventionMapping.outputDir = { ext.outputDir == null ? project.buildDir : ext.outputDir }
            task.conventionMapping.credentials = { ext.credentials }
        }
    }
}
