package org.github.mansur.oozie.extensions

import java.util.Map;

/**
 * @author Muhammad Ashraf
 * @since 7/27/13
 */
class OozieWorkflowExtension {
    String name
    String end
    String namespace
    HashMap<String, Object> common
    HashMap<String, Object> jobXML
    List<HashMap<String, Object>> actions
    Map<String, Map<String, String>> credentials
    File outputDir
}
