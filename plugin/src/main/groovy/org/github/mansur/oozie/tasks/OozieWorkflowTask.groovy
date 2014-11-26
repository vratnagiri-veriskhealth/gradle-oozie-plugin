package org.github.mansur.oozie.tasks

import org.github.mansur.oozie.beans.CredentialNode
import org.github.mansur.oozie.beans.SlaNode
import org.github.mansur.oozie.beans.WorkflowNode
import org.github.mansur.oozie.builders.WorkFlowBuilder
import org.github.mansur.oozie.extensions.OozieWorkflowExtension
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
  
	OozieWorkflowExtension extension
    @Input @Optional CopySpec workflowFiles

    OozieWorkflowTask() {
        group = "Oozie"
    }

    protected CopySpec includeFiles(Closure closure) {
      workflowFiles = project.copySpec(closure)
    }

    String getDescription() {
      "Generate Oozie workflow"
    }

    @TaskAction
    void start() {
		//extension.fixExtension()
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
		
        generateFlow(builder)
        //generateJobXML(builder, actualWorkflowFileName)
    }

    private void generateJobXML(WorkFlowBuilder builder, String actualWorkflowFileName) {
      if (! jobXML.isEmpty()) {
        def outputFile = new File(getOutputDir().absolutePath, actualWorkflowFileName + "-config.xml")
        outputFile.parentFile.mkdirs()
        println("generating oozie job xml: file://$outputFile")
        outputFile.write(builder.buildJobXML(jobXML))
      }
    }

    private void generateFlow(WorkFlowBuilder builder) {
      String xml

	  try {
		  assert extension,"extension is null"
          xml = builder.buildWorkflow(extension)
      } catch (Exception e) {
          throw new GradleException(e.message, e)
      }

      def outputFile = new File(extension.getOutputDir().absolutePath, extension.name+"/workflow.xml")
      outputFile.parentFile.mkdirs()
      println("generating oozie workflow: file://$outputFile")
      outputFile.write(xml)
    }
}
