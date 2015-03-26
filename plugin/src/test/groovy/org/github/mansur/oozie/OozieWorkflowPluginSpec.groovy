package org.github.mansur.oozie

import java.nio.file.Files;

import org.github.mansur.oozie.beans.*
import org.github.mansur.oozie.extensions.OozieWorkflowExtension
import org.github.mansur.oozie.plugin.OozieWorkflowPlugin
import org.github.mansur.oozie.tasks.OozieWorkflowTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit

import com.google.common.base.Charsets;

/**
 * @author Muhammad Ashraf
 * @since 7/27/13
 */
class OozieWorkflowPluginSpec extends Specification {

	static final String EXTENSION_NAME = 'oozie'
	static final String TASK_NAME = 'oozieTask'

	Project project

	def setup() {
		project = ProjectBuilder.builder().build()
	}

	def "Apply oozie plugin with beans"() {
		expect:
		project.tasks.findByName(TASK_NAME) == null

		when:
		project.apply plugin: 'oozie'
		project.task(TASK_NAME, type: OozieWorkflowTask)
		OozieWorkflowTask task = project.tasks.findByName(TASK_NAME)

		def oozie = project.oozie;
		def jobTracker = "http://jobtracker"
		def namenode = "http://namenode"

		oozie.start='shell_to_prod'
		oozie.global{
			[jobTracker: "$jobTracker",
				nameNode: "$namenode",
				jobXml: "dev_prop.xml"]
		}

		def credentials =
				oozie.hcatCredentials{
					[name: 'hive_credentials',
						metastoreUri: "thrift://localhost:9083/",
						metastorePrincipal: "hive/_HOST@DOMAIN"]
				}

		def shell_to_prod = oozie.shell{
			[name: "shell_to_prod",
				ok: "fork_flow",
				error: "fail",
				delete: ["/tmp/workDir"],
				mkdir: ["/tmp/workDir"],
				exec: "ssh test@localhost",
				captureOutput: true,
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				],
				args: [
					"input",
					"output",
					"cache.txt"
				],
				env: ["java_home"],
				file: [
					"file1.txt",
					"file2.txt"
				],
				archive: ["job.tar"]]
		}


		def pig_job = oozie.pig{
			[name: "pig_job",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "hive_job",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				],
				script: "first.pig",
				params: [
					"--input",
					"/cart",
					"--output",
					"--maxheapSize",
					"50"
				]]
		}

		def fork_flow = oozie.forkJoin{
			def move_files = fs{
				[name: "move_files",
					ok: "fork_flow",
					error: "fail",
					delete: [
						"hdfs://foo:9000/usr/tucu/temp-data"
					],
					mkdir: ['archives/${wf:id()}'],
					move: [
						[source: '${jobInput}', target: 'archives/${wf:id()}/processed-input'],
						[source: '${jobInput}', target: 'archives/${wf:id()}/raw-input' ]
					],

					chmod: [
						[ path: '${jobOutput}', permissions: '-rwxrw-rw-', dirFiles: true ]
					]
				]
			}

			def mahout_pfpgrowth = java{
				[name: "mahout_fp_growth",
					delete: ["${jobTracker}/pattern"],
					mainClass: "some.random.class",
					jobXml: "job.xml",
					ok: "fork_flow",
					error: "fail",
					configuration: [
						"mapred.map.output.compress": "false",
						"mapred.job.queue.name": "queuename"
					],
					args: [
						"--input",
						"/cart",
						"--output",
						"--maxheapSize",
						"50"
					]]
			}
			[name: "fork_flow",
				to:"pig_job"
			]
		}

		/*def sub_workflow_job = oozie.subWorkflow{
		 [name: "sub_workflow_job",
		 appPath: "hdfs://foo:9000/usr/tucu/temp-data",
		 ok: "hive_job",
		 error: "fail",
		 configuration: [
		 "property.name": "property.value",
		 "property1.name": "property1.value"
		 ]]
		 }
		 */
		def hive_job = oozie.hive{
			[name: "hive_job",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "authenticated_hive_job",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				],
				script: "first.hql",
				params: [ schema: 'standard', otherParam: 'other value' ]
			]
		}

		def authenticated_hive_job = oozie.hive{
			[name: "authenticated_hive_job",
				cred: "hive_credentials",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "flow_decision",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				],
				script: "first.hql",
				params: [ schema: 'standard', otherParam: 'other value' ]
			]
		}

		def first_map_reduce = oozie.mapReduce{
			[name: "first_map_reduce",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "end",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				]
			]
		}


		def flow_decision = oozie.decision{
			[name: "flow_decision",
				decisions: ['some condition':'end',
					'some other condition':'first_map_reduce'
				],
				defaultDecision: 'end']
		}

		def fail = oozie.kill{
			[name: "fail",
				message: "workflow failed!"]
		}

		authenticated_hive_job >> first_map_reduce;

		/*oozie.actions = [
		 shell_to_prod,
		 fork_flow,
		 pig_job,
		 hive_job,
		 authenticated_hive_job,
		 first_map_reduce,
		 flow_decision,
		 fail
		 ]*/

		oozie.name 'oozie_flow'

		oozie.start 'blahblah'
		/*oozie.credentials = [
		 oozie.hcatCredentials{
		 [name: 'hive_credentials',
		 metastoreUri: "thrift://localhost:9083/",
		 metastorePrincipal: "hive/_HOST@DOMAIN"
		 ]
		 }
		 ]*/

		oozie.outputDir=new File("$project.buildDir")

		then:

		task.extension.getActions().size() == 8

		and:
		task.start()
		def xml=new File("$project.buildDir/oozie_flow/workflow.xml")
		xml.exists()
		def result = xml.readLines().join("")
		XMLUnit.setIgnoreWhitespace(true)

		BuilderTestUtils.assertXml(SAMPLE_XML.EXPECTED_FLOW, result)
	}

	def "oozie sample"(){
		expect:
		project.tasks.findByName(TASK_NAME) == null

		when:
		project.apply plugin: 'oozie'
		project.task(TASK_NAME, type: OozieWorkflowTask)
		OozieWorkflowTask task = project.tasks.findByName(TASK_NAME)

		def oozie = project.oozie;
		def String jobTrack="http://jobtracker";

		oozie.global{
			jobTracker "http://jobtracker"
			nameNode "http://namenode"
			configuration{
				["mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"]
			}
		}

		def mr=oozie.mapReduce{
			name "first_map_reduce"
			delete (["${jobTrack}/pattern"])
			configuration{
				[
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				]
			}
		} 
		def fr=oozie.forkJoin{
			name "forkjoin"
			startActions ([
				"map_reduce_fork1_1",
				"map_reduce_fork2_1"
			]);
			
			def mr11=mapReduce{
				name "map_reduce_fork1_1"
				delete (["${jobTrack}/pattern"])
				configuration{
					[
						"mapred.map.output.compress": "false",
						"mapred.job.queue.name": "queuename"
					]
				}
			} 
			def mr12 =mapReduce{
				name "map_reduce_fork1_2"
				delete (["${jobTrack}/pattern"])
				configuration{
					[
						"mapred.map.output.compress": "false",
						"mapred.job.queue.name": "queuename"
					]
				}
			}
			def mr21=mapReduce{
				name "map_reduce_fork2_1"
				delete (["${jobTrack}/pattern"])
				configuration{
					[
						"mapred.map.output.compress": "false",
						"mapred.job.queue.name": "queuename"
					]
				}
			}
			mr11>>mr12
			
		}
		def ed=oozie.end("end")
		mr>>fr>>ed
		oozie.name 'oozie_flow'
		oozie.start "first_map_reduce"
		oozie.fail "fail"
		oozie.outputDir = project.file("$project.buildDir")
		then:

		//task.extension.getActions().size() == 8

		//and:
		task.start()
		def xml=new File("$project.buildDir/oozie_flow/workflow.xml")
		xml.exists()
		def result = xml.readLines().join("")
		XMLUnit.setIgnoreWhitespace(true)

		BuilderTestUtils.assertXml(SAMPLE_XML.EXPECTED_FLOW, result)
	}

	def "noncyclic DAG"() {
		//       flow_decision
		//          /   |  \
		//         m2   |   \
		//           \  |    \
		//            \ v     \
		//             m3---->m4--->end_node
		expect:
		project.tasks.findByName(TASK_NAME) == null

		when:
		project.apply plugin: 'oozie'
		project.task(TASK_NAME, type: OozieWorkflowTask)
		OozieWorkflowTask task = project.tasks.findByName(TASK_NAME)

		def oozie = project.oozie;

		def jobTracker = "http://jobtracker"
		def namenode = "http://namenode"

		oozie.global = oozie.global{
			[jobTracker: "$jobTracker",
				nameNode: "$namenode",
				jobXml: "dev_prop.xml"
			]
		}

		def flow_decision = oozie.decision{
			[name: "flow_decision",
				decisions: [
					"some condition":"mr1",
					"some other condition":"mr2"
				],
				defaultDecision: "mr4"
			]}

		def mr1 = oozie.mapReduce{
			[name: "mr1",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "mr3",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				]]}

		def mr2 = oozie.mapReduce{
			[name: "mr2",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "mr3",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				]
			]
		}

		def mr3 = oozie.mapReduce{
			[name: "mr3",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "mr4",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				]
			]
		}

		def mr4 = oozie.mapReduce{
			[name: "mr4",
				delete: ["${jobTracker}/pattern"],
				jobXml: "job.xml",
				ok: "end",
				error: "fail",
				configuration: [
					"mapred.map.output.compress": "false",
					"mapred.job.queue.name": "queuename"
				]
			]
		}

		def fail = oozie.kill{
			[name: "fail",
				message: "workflow failed!"
			]
		}

		oozie.actions = [
			flow_decision,
			mr1,
			mr2,
			mr3,
			mr4,
			fail
		]
		oozie.start='flow_decision'
		oozie.name='oozie_flow'
		oozie.outputDir=new File("$project.buildDir")

		then:
		project.extensions.findByName(EXTENSION_NAME) != null

		and:
		task.start()
		def xml=new File("$project.buildDir/oozie_flow/workflow.xml")
		xml.exists()
	}

	//	def "alternate filename"() {
	//		expect:
	//		project.tasks.findByName(TASK_NAME) == null
	//
	//		when:
	//		project.apply plugin: 'oozie'
	//		project.task(TASK_NAME, type: OozieWorkflowTask)
	//		OozieWorkflowTask task = project.tasks.findByName(TASK_NAME)
	//
	//		def oozie = project.oozie;
	//		def jobTracker = "http://jobtracker"
	//		def namenode = "http://namenode"
	//
	//		task.workflowActions = []
	//		task.jobXML = ["key" : "value"]
	//
	//		task.common = oozie.common(
	//				jobTracker: "$jobTracker",
	//				nameNode: "$namenode",
	//				jobXml: "dev_prop.xml"
	//				)
	//
	//		task.name = 'oozie_flow'
	//		task.namespace = 'uri:oozie:workflow:0.1'
	//
	//		task.workflowName = 'oozie_flow'
	//		task.workflowFileName = 'otherName'
	//		task.end = "end_node"
	//
	//		then:
	//
	//		task.start()
	//		def xml=new File("$project.buildDir/otherName.xml")
	//		xml.exists()
	//		def result = xml.readLines().join("")
	//		XMLUnit.setIgnoreWhitespace(true)
	//		def xmlOut = new XmlParser().parseText(result)
	//		println "xmlOut: ${xmlOut.getClass()} ${xmlOut}"
	//		and:
	//		xmlOut.attribute("name") == "oozie_flow"
	//		and:
	//		new File("$project.buildDir/otherName-config.xml").exists()
	//	}

	//	def "workflow files"() {
	//		expect:
	//		project.tasks.findByName(TASK_NAME) == null
	//
	//		when:
	//		project.apply plugin: 'oozie'
	//		project.task(TASK_NAME, type: OozieWorkflowTask)
	//		OozieWorkflowTask task = project.tasks.findByName(TASK_NAME)
	//		def hqlDir = new File(project.rootDir, "hql")
	//		hqlDir.mkdir()
	//		Files.write(hqlDir.toPath().resolve("foo.hql"), ['select * from world'], Charsets.UTF_8)
	//
	//		def oozie = project.oozie;
	//		def jobTracker = "http://jobtracker"
	//		def namenode = "http://namenode"
	//
	//		task.workflowActions = []
	//		task.jobXML = ["key" : "value"]
	//
	//		task.common = oozie.common(
	//				jobTracker: "$jobTracker",
	//				nameNode: "$namenode",
	//				jobXml: "dev_prop.xml"
	//				)
	//
	//		task.name = 'oozie_flow'
	//		task.namespace = 'uri:oozie:workflow:0.1'
	//
	//		task.workflowName = 'oozie_flow'
	//		task.end = "end_node"
	//		task.includeFiles {
	//			from "hql"
	//			into "sql"
	//		}
	//		task.outputDir(new File(project.buildDir, "workflow"))
	//		then:
	//
	//		task.start()
	//		def xml=new File(task.outputDir, "oozie_flow.xml")
	//		xml.exists()
	//		def result = xml.readLines().join("")
	//		XMLUnit.setIgnoreWhitespace(true)
	//		def xmlOut = new XmlParser().parseText(result)
	//		println "xmlOut: ${xmlOut.getClass()} ${xmlOut}"
	//		and:
	//		xmlOut.attribute("name") == "oozie_flow"
	//		and:
	//		new File(task.outputDir, "oozie_flow-config.xml").exists()
	//		def sqlFile = new File(task.outputDir, "sql/foo.hql")
	//		sqlFile.exists()
	//		Files.readAllLines(sqlFile.toPath(), Charsets.UTF_8) == ["select * from world"]
	//	}
}
