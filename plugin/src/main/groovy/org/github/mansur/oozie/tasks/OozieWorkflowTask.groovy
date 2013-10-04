package org.github.mansur.oozie.tasks

import org.github.mansur.oozie.beans.CommonProperties
import org.github.mansur.oozie.beans.CredentialNode
import org.github.mansur.oozie.beans.SlaNode
import org.github.mansur.oozie.beans.WorkflowNode
import org.github.mansur.oozie.builders.WorkFlowBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * @author Muhammad Ashraf
 * @since 7/27/13
 */
class OozieWorkflowTask extends DefaultTask {

    @Input String workflowName
    @Input String end
    @Input String namespace = 'uri:oozie:workflow:0.1'
    @Input @Optional CommonProperties common
    @Input @Optional HashMap<String, Object> jobXML = [:]
    @Input List<WorkflowNode> workflowActions
    @Input @Optional List<CredentialNode> credentials = null
    @Input File outputDir = project.buildDir
    @Input @Optional SlaNode sla = null

    OozieWorkflowTask() {
        description = "Generates Ozzie workflow"
        group = "Oozie"
    }

    @TaskAction
    void start() {
        generateWorkflow()
    }

    private void generateWorkflow() {
        def builder = new WorkFlowBuilder()
        generateFlow(builder)
        generateJobXML(builder)
    }

    private void generateJobXML(WorkFlowBuilder builder) {
      if (! jobXML.isEmpty()) {
        def outputFile = new File(getOutputDir().absolutePath + File.separator + getWorkflowName() + "-config.xml")
        outputFile.parentFile.mkdirs()
        println("generating oozie job xml : file://$outputFile")
        outputFile.write(builder.buildJobXML(jobXML))
      }
    }

    private void generateFlow(WorkFlowBuilder builder) {
        String xml

        try {
            xml = builder.buildWorkflow(this)
        } catch (Exception e) {
            throw new GradleException(e.message, e)
        }

        def outputFile = new File(getOutputDir().absolutePath + File.separator + getWorkflowName() + ".xml")
        outputFile.parentFile.mkdirs()
        println("generating oozie workflow: file://$outputFile")
        outputFile.write(xml)
    }
}
