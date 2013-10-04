package org.github.mansur.oozie.extensions

import java.util.Map;
import org.github.mansur.oozie.beans.*

/**
 * @author Muhammad Ashraf
 * @since 7/27/13
 */
class OozieWorkflowExtension {
    String name
    String end
    String namespace
    Object common
    HashMap<String, Object> jobXML
    List<HashMap<String, Object>> actions
    Object credentials
    File outputDir

    def SubWorkflowNode subWorkflow(params) { new SubWorkflowNode(params) }
    def HiveNode hive(params) { new HiveNode(params) }
    def PigNode pig(params) { new PigNode(params) }
    def JavaNode java(params) { new JavaNode(params) }
    def MapReduceNode mapReduce(params)  { new MapReduceNode(params) }
    def ShellNode shell(params)  { new ShellNode(params) }
    def SshNode ssh(params)  { new SshNode(params) }
    def FsNode fs(params) { new FsNode(params) }
    def FsMoveNode fsMove(params) { new FsMoveNode(params) }
    def FsChmodNode fsChmod(params) { new FsChmodNode(params) }
    def EmailNode email(params) { new EmailNode(params) }
    def KillNode kill(params) { new KillNode(params) }
    def KillNode kill(String name, String message) { new KillNode(name: name, message: message) }
    def DecisionCaseNode decisionCase(params) { new DecisionCaseNode(params) }
    def DecisionCaseNode decisionCase(String to, String condition) { new DecisionCaseNode([to: to, condition: condition]) }
    def DecisionNode decision(params) { new DecisionNode(params) }
    def ForkNode fork(params) { new ForkNode(params) }
    def ForkNode fork(String name, List<String> paths) { new ForkNode(name: name, paths: paths) }
    def JoinNode join(params) { new JoinNode(params) }
    def JoinNode join(String name, String to) { new JoinNode(name: name, to: to) }
    def CommonProperties common(params) { new CommonProperties(params) }
    def HcatCredentialNode hcatCredentials(params) { new HcatCredentialNode(params) }
    def SlaNode sla(params) { new SlaNode(params) }
}
