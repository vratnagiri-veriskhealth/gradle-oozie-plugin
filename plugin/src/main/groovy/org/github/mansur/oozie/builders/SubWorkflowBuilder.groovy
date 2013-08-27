/*
 * Copyright 2013. Muhammad Ashraf
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.github.mansur.oozie.builders

import java.util.HashMap;

import groovy.xml.MarkupBuilder

/**
 * @author Seba Jean-Baptiste
 * @since 8/01/13
 */
class SubWorkflowBuilder extends BaseBuilder {
  def buildXML(MarkupBuilder xml, HashMap<String, Object> action, HashMap<String, Object> common) {
    HashMap<String, Object> map = getMergedProperties(common, action)
    xml.action(name: map.get(NAME)) {
      'sub-workflow' {
        addNode(map, xml, 'app-path', APP_PATH)
        addPropagateConfiguration(xml, map)
        xml.configuration { addConfiguration(xml, map) }
      }
      addOkOrError(xml, map, "ok")
      addOkOrError(xml, map, "error")
    }
  }

  def addPropagateConfiguration(MarkupBuilder xml, HashMap<String, Object> map) {
    if (map.containsKey(PROPAGATE_CONFIGURATION) && map.get(PROPAGATE_CONFIGURATION) == true) {
      xml.'propagate-configuration'()
    }
  }

}
