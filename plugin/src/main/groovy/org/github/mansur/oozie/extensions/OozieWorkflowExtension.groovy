package org.github.mansur.oozie.extensions

import java.util.Map;
import org.github.mansur.oozie.beans.CommonProperties
import org.github.mansur.oozie.beans.DecisionCaseNode
import org.github.mansur.oozie.beans.DecisionNode
import org.github.mansur.oozie.beans.EmailNode
import org.github.mansur.oozie.beans.ForkNode
import org.github.mansur.oozie.beans.FsChmodNode
import org.github.mansur.oozie.beans.FsMoveNode
import org.github.mansur.oozie.beans.FsNode
import org.github.mansur.oozie.beans.HcatCredentialNode
import org.github.mansur.oozie.beans.HiveNode
import org.github.mansur.oozie.beans.SubWorkflowNode
import org.github.mansur.oozie.beans.JavaNode
import org.github.mansur.oozie.beans.JoinNode
import org.github.mansur.oozie.beans.KillNode
import org.github.mansur.oozie.beans.MapReduceNode
import org.github.mansur.oozie.beans.PigNode
import org.github.mansur.oozie.beans.ShellNode
import org.github.mansur.oozie.beans.SshNode

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
    Map<String, Map<String, String>> credentials
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
}
