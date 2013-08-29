package org.github.mansur.oozie

import org.github.mansur.oozie.beans.*
import org.github.mansur.oozie.plugin.OozieWorkflowPlugin
import org.github.mansur.oozie.tasks.OozieWorkflowTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

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

    def "Apply oozie plugin"() {
        expect:
        project.tasks.findByName(TASK_NAME) == null

        when:
        project.apply plugin: 'oozie'

        project.oozie {
            def jobTracker = "http://jobtracker"
            def namenode = "http://namenode"

            def common_props = [
                    jobTracker: "$jobTracker",
                    namenode: "$namenode",
                    jobXML: "dev_prop.xml"
            ]

            def shell_to_prod = [
                    name: "shell_to_prod",
                    type: 'shell',
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
                    envVar: [
                            "java_home"
                    ],
                    file: [
                            "file1.txt",
                            "file2.txt"
                    ],
                    archive: [
                            "job.tar"
                    ]
            ]

            def move_files = [
                    name: "move_files",
                    type: 'fs',
                    ok: "join_flow",
                    error: "fail",
                    delete: ["hdfs://foo:9000/usr/tucu/temp-data"],
                    mkdir: ['archives/${wf:id()}'],
                    move: [
                            [source: '${jobInput}', target: 'archives/${wf:id()}/processed-input'],
                            [source: '${jobInput}', target: 'archives/${wf:id()}/raw-input']

                    ],
                    chmod: [
                            [path: '${jobOutput}', permissions: '-rwxrw-rw-', dir_files: 'true']
                    ]
            ]

            def mahout_pfpgrowth = [
                    name: "mahout_fp_growth",
                    type: "java",
                    delete: ["${jobTracker}/pattern"],
                    mainClass: "some.random.class",
                    jobXML: "job.xml",
                    ok: "join_flow",
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
                    ]
            ]

            def fork_flow = [
                    name: "fork_flow",
                    type: "fork",
                    paths: [
                            "move_files",
                            "mahout_fp_growth"
                    ]
            ]

            def join_flow = [
                    name: "join_flow",
                    type: "join",
                    to: "pig_job"
            ]

            def pig_job = [
                    name: "pig_job",
                    type: "pig",
                    delete: ["${jobTracker}/pattern"],
                    mainClass: "some.random.class",
                    jobXML: "job.xml",
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
                    ]
            ]

            def hive_job = [
                    name: "hive_job",
                    type: "hive",
                    delete: ["${jobTracker}/pattern"],
                    mainClass: "some.random.class",
                    jobXML: "job.xml",
                    ok: "flow_decision",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ],
                    script: "first.hql",
                    params: [
                            "--input",
                            "/cart",
                            "--output",
                            "--maxheapSize",
                            "50"
                    ]
            ]

            def email_job = [
                    name: "email_job",
                    type: "email",
                    to: "recipient@mail.com",
                    cc: "also@mail.com",
                    subject: "vasal",
                    body: "bow down",
                    ok: "flow_decision",
                    error: "fail",
            ]

            def first_map_reduce = [
                    name: "first_map_reduce",
                    type: "mapreduce",
                    delete: ["${jobTracker}/pattern"],
                    jobXML: "job.xml",
                    ok: "end_node",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ]
            ]

            def flow_decision = [
                    name: "flow_decision",
                    type: "decision",
                    switch: [
                            [to: "end_node", if: "some condition"],
                            [to: "first_map_reduce", if: "some other condition"]
                    ],
                    default: "end_node"
            ]

            def fail = [
                    name: "fail",
                    type: "kill",
                    message: "workflow failed!"
            ]


            actions = [
                    shell_to_prod,
                    move_files,
                    mahout_pfpgrowth,
                    fork_flow,
                    join_flow,
                    pig_job,
                    hive_job,
                    first_map_reduce,
                    flow_decision,
                    fail]

            common = common_props
            end = "end_node"
            name = 'oozie_flow'
            namespace = 'uri:oozie:workflow:0.1'
        }

        project.task(TASK_NAME, type: OozieWorkflowTask)

        then:
        project.extensions.findByName(EXTENSION_NAME) != null
        Task task = project.tasks.findByName(TASK_NAME)
        task.end == "end_node"
        task.workflowName == 'oozie_flow'
        task.namespace == 'uri:oozie:workflow:0.1'
        task.common.size() == 3
        task.workflowActions.size() == 10

        and:
        task.start()
        def xml=new File("$project.buildDir/oozie_flow.xml")
        xml.exists()

    }

    def "Apply oozie plugin with beans"() {
        expect:
        project.tasks.findByName(TASK_NAME) == null

        when:
        project.apply plugin: 'oozie'

        project.oozie {
            def jobTracker = "http://jobtracker"
            def namenode = "http://namenode"

            def common_props = common (
              jobTracker: "$jobTracker",
              nameNode: "$namenode",
              jobXml: "dev_prop.xml"
            )

            def shell_to_prod = shell(
                name: "shell_to_prod",
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
                env: [
                        "java_home"
                ],
                file: [
                        "file1.txt",
                        "file2.txt"
                ],
                archive: [
                        "job.tar"
                ]
            )

            def move_files = fs(
                name: "move_files",
                ok: "join_flow",
                error: "fail",
                delete: ["hdfs://foo:9000/usr/tucu/temp-data"],
                mkdir: ['archives/${wf:id()}'],
                move: [ fsMove( source: '${jobInput}', target: 'archives/${wf:id()}/processed-input' ),
                        fsMove( source: '${jobInput}', target: 'archives/${wf:id()}/raw-input' ) ],
                chmod: [fsChmod( path: '${jobOutput}', permissions: '-rwxrw-rw-', dirFiles: true ) ]
            )

            def mahout_pfpgrowth = java(
                    name: "mahout_fp_growth",
                    delete: ["${jobTracker}/pattern"],
                    mainClass: "some.random.class",
                    jobXml: "job.xml",
                    ok: "join_flow",
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
                    ]
            )

            def fork_flow = fork("fork_flow", [ "move_files", "mahout_fp_growth" ])

            def join_flow = join ( "join_flow", "pig_job" )

            def pig_job = pig(
                    name: "pig_job",
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
                    ]
            )

            def hive_job = hive(
                    name: "hive_job",
                    delete: ["${jobTracker}/pattern"],
                    jobXml: "job.xml",
                    ok: "flow_decision",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ],
                    script: "first.hql",
                    params: [ schema: "standard", otherParam: 'other value' ]
            )

            def email_job = email(
                    name: "email_job",
                    to: "recipient@mail.com",
                    cc: "also@mail.com",
                    subject: "vasal",
                    body: "bow down",
                    ok: "flow_decision",
                    error: "fail",
            )

            def first_map_reduce = mapReduce(
                    name: "first_map_reduce",
                    delete: ["${jobTracker}/pattern"],
                    jobXml: "job.xml",
                    ok: "end_node",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ]
            )

            def flow_decision = decision(
                    name: "flow_decision",
                    cases: [
                            decisionCase("end_node", "some condition"),
                            decisionCase("first_map_reduce", "some other condition")
                    ],
                    defaultCase: "end_node"
            )

            def fail = kill(name: "fail", message: "workflow failed!")

            actions = [
                    shell_to_prod,
                    move_files,
                    mahout_pfpgrowth,
                    fork_flow,
                    join_flow,
                    pig_job,
                    hive_job,
                    first_map_reduce,
                    flow_decision,
                    fail]

            common = common_props
            end = "end_node"
            name = 'oozie_flow'
            namespace = 'uri:oozie:workflow:0.1'
            credentials = [
              hcatCredentials(
                name: 'hive_credentials',
                metastoreUri: "thrift://localhost:9083/",
                metastorePrincipal: "hive/_HOST@DOMAIN"
              )
            ]

        }

        project.task(TASK_NAME, type: OozieWorkflowTask)

        then:
        project.extensions.findByName(EXTENSION_NAME) != null
        Task task = project.tasks.findByName(TASK_NAME)
        task.end == "end_node"
        task.workflowName == 'oozie_flow'
        task.namespace == 'uri:oozie:workflow:0.1'
        task.common.toMap().size() == 3
        task.workflowActions.size() == 10

        and:
        task.start()
        def xml=new File("$project.buildDir/oozie_flow.xml")
        xml.exists()

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

        project.oozie {
            def jobTracker = "http://jobtracker"
            def namenode = "http://namenode"

            def common_props = [
                    jobTracker: "$jobTracker",
                    namenode: "$namenode",
                    jobXML: "dev_prop.xml"
            ]

            def flow_decision = [
                    name: "flow_decision",
                    type: "decision",
                    switch: [
                            [to: "mr1", if: "some condition"],
                            [to: "mr2", if: "some other condition"]
                    ],
                    default: "mr4"
            ]

            def mr1 = [
                    name: "mr1",
                    type: "mapreduce",
                    delete: ["${jobTracker}/pattern"],
                    jobXML: "job.xml",
                    ok: "mr3",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ]
            ]

            def mr2 = [
                    name: "mr2",
                    type: "mapreduce",
                    delete: ["${jobTracker}/pattern"],
                    jobXML: "job.xml",
                    ok: "mr3",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ]
            ]

            def mr3 = [
                    name: "mr3",
                    type: "mapreduce",
                    delete: ["${jobTracker}/pattern"],
                    jobXML: "job.xml",
                    ok: "mr4",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ]
            ]

            def mr4 = [
                    name: "mr4",
                    type: "mapreduce",
                    delete: ["${jobTracker}/pattern"],
                    jobXML: "job.xml",
                    ok: "end_node",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ]
            ]

            def fail = [
                    name: "fail",
                    type: "kill",
                    message: "workflow failed!"
            ]


            actions = [ flow_decision, mr1, mr2, mr3, mr4, fail ]

            common = common_props
            end = "end_node"
            name = 'oozie_flow'
            namespace = 'uri:oozie:workflow:0.1'
        }

        project.task(TASK_NAME, type: OozieWorkflowTask)

        then:
        project.extensions.findByName(EXTENSION_NAME) != null
        Task task = project.tasks.findByName(TASK_NAME)
        task.end == "end_node"
        task.workflowName == 'oozie_flow'
        task.namespace == 'uri:oozie:workflow:0.1'
        task.common.size() == 3
        task.workflowActions.size() == 6

        and:
        task.start()
        def xml=new File("$project.buildDir/oozie_flow.xml")
        xml.exists()

    }

    def "Apply oozie with credentials"() {
        expect:
        project.tasks.findByName(TASK_NAME) == null

        when:
        project.apply plugin: 'oozie'

        project.oozie {
            def jobTracker = "http://jobtracker"
            def namenode = "http://namenode"

            def common_props = [
                    jobTracker: "$jobTracker",
                    namenode: "$namenode",
                    jobXML: "dev_prop.xml"
            ]

            def hive_job = [
                    cred: "hive_credentials",
                    name: "hive_job",
                    type: "hive",
                    delete: ["${jobTracker}/pattern"],
                    mainClass: "some.random.class",
                    jobXML: "job.xml",
                    ok: "end_node",
                    error: "fail",
                    configuration: [
                            "mapred.map.output.compress": "false",
                            "mapred.job.queue.name": "queuename"
                    ],
                    script: "first.hql",
                    params: [
                            "--input",
                            "/cart",
                            "--output",
                            "--maxheapSize",
                            "50"
                    ]
            ]

            def fail = [
                    name: "fail",
                    type: "kill",
                    message: "workflow failed!"
            ]


            actions = [
                    hive_job,
                    fail]

            common = common_props
            end = "end_node"
            name = 'oozie_flow'
            namespace = 'uri:oozie:workflow:0.1'
            credentials = [
                    "hive_credentials": [
                      "hcat.metastore.uri": "thrift://localhost:9083/",
                      "hcat.metastore.principal": "hive/_HOST@DOMAIN"
                    ],
                    "other_credentials": [
                      "hcat.metastore.uri": "thrift://otherhost:9083/",
                      "hcat.metastore.principal": "hive/_HOST@OTHERDOMAIN"
                    ],
            ]
        }

        project.task(TASK_NAME, type: OozieWorkflowTask)

        then:
        project.extensions.findByName(EXTENSION_NAME) != null
        Task task = project.tasks.findByName(TASK_NAME)
        task.end == "end_node"
        task.workflowName == 'oozie_flow'
        task.namespace == 'uri:oozie:workflow:0.1'
        task.common.size() == 3
        task.workflowActions.size() == 2
        task.credentials.size() == 2

        and:
        task.start()
        def xml=new File("$project.buildDir/oozie_flow.xml")
        xml.exists()

    }
}
