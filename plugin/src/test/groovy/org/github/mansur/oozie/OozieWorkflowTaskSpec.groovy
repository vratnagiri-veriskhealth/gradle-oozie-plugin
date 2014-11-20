package org.github.mansur.oozie

import org.github.mansur.oozie.plugin.OozieWorkflowPlugin
import org.github.mansur.oozie.tasks.OozieWorkflowTask
import org.gradle.api.Project
import org.gradle.api.internal.file.TmpDirTemporaryFileProvider;
import org.gradle.testfixtures.ProjectBuilder

import com.google.common.base.Charsets;

import java.nio.file.Files
import java.nio.file.attribute.FileAttribute
import java.nio.file.Path

import spock.lang.Specification

/**
 * @author Muhammad Ashraf
 * @since 7/27/13
 */
class OozieWorkflowTaskSpec extends Specification {
  def noAttrs = new FileAttribute[0]

  Project project = ProjectBuilder.builder().build()
  Path tempDir = Files.createTempDirectory("oozieTest", noAttrs)
  Path sourceDir = tempDir.resolve("source")
  Path altSourceDir = tempDir.resolve("altSource")
  Path targetDir = tempDir.resolve("target")

  static final String TASK_NAME = 'oozieTask'

  def setup() {
    Files.createDirectory(sourceDir, noAttrs)
    Files.createDirectory(targetDir, noAttrs)
    Files.write(sourceDir.resolve("a"), Arrays.asList("hello", "goodbye"), Charsets.UTF_8)
    Files.write(sourceDir.resolve("b"), Arrays.asList("more"), Charsets.UTF_8)
    Files.createDirectory(altSourceDir, noAttrs)
    Files.write(altSourceDir.resolve("c"), Arrays.asList("sub"), Charsets.UTF_8)
  }

  def cleanup() {
    tempDir.toFile().deleteDir()
  }

  def "Add oozie task"() {
    expect:
    project.tasks.findByName(TASK_NAME) == null

    when:
    project.task(TASK_NAME, type: OozieWorkflowTask) {
      workflowName = "some_oozie_flow"
      start = "start_node"
      end = "end_node"
      jobXML = [key: "value"]
      workflowActions = ["action1", "action2"]
      outputDir = targetDir.toFile()
      includeFiles {
        from sourceDir.toString()
        into('sub') { from altSourceDir.toString() }
      }
    }

    then:
    def task = project.tasks.findByName(TASK_NAME)
    task != null
    task.description == "Generate Oozie workflow some_oozie_flow"
    task.workflowName == "some_oozie_flow"
    task.start == "start_node"
    task.end == "end_node"
    task.jobXML == [key: "value"]
    task.workflowActions == ["action1", "action2"]
    task.outputDir == targetDir.toFile()

    // The following depend on internal details of the CopySpec implementation, and hence may break as Gradle evolves
    task.workflowFiles.sourcePaths == [ sourceDir.toString() ] as Set
    def children = task.workflowFiles.hasProperty("childSpecs") ?
      task.workflowFiles.childSpecs :
      task.workflowFiles.getChildren()
    children.size() == 1
    def subSpec = children.get(0)
    subSpec.sourcePaths == [ altSourceDir.toString() ] as Set
    //subSpec.getDestPath().toString() == "sub"
  }
}
