package org.github.mansur.oozie.tasks

import org.github.mansur.oozie.beans.CommonProperties
import org.github.mansur.oozie.beans.CredentialNode
import org.github.mansur.oozie.beans.SlaNode
import org.github.mansur.oozie.beans.WorkflowNode
import org.github.mansur.oozie.builders.WorkFlowBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.CopySpec
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
    @Input @Optional workflowFileName = null
    @Input @Optional CopySpec workflowFiles

    OozieWorkflowTask() {
        group = "Oozie"
    }

    protected CopySpec includeFiles(Closure closure) {
      workflowFiles = project.copySpec(closure)
    }

    String getDescription() {
      "Generate Oozie workflow ${workflowName}"
    }

    @TaskAction
    void start() {
        generateWorkflow()
        copyWorfklowFiles()
    }

    private copyWorfklowFiles() {
      if (workflowFiles != null) {
        project.copy {
          with workflowFiles
          into outputDir
        }
      }
    }

    private void generateWorkflow() {
        def builder = new WorkFlowBuilder()
        def actualWorkflowFileName = workflowFileName ?: workflowName
        generateFlow(builder, actualWorkflowFileName)
        generateJobXML(builder, actualWorkflowFileName)
    }

    private void generateJobXML(WorkFlowBuilder builder, String actualWorkflowFileName) {
      if (! jobXML.isEmpty()) {
        def outputFile = new File(getOutputDir().absolutePath, actualWorkflowFileName + "-config.xml")
        outputFile.parentFile.mkdirs()
        println("generating oozie job xml: file://$outputFile")
        outputFile.write(builder.buildJobXML(jobXML))
      }
    }

    private void generateFlow(WorkFlowBuilder builder, String actualWorkflowFileName) {
      String xml

      try {
          xml = builder.buildWorkflow(this)
      } catch (Exception e) {
          throw new GradleException(e.message, e)
      }

      def outputFile = new File(getOutputDir().absolutePath, actualWorkflowFileName + ".xml")
      outputFile.parentFile.mkdirs()
      println("generating oozie workflow: file://$outputFile")
      outputFile.write(xml)
    }
}
