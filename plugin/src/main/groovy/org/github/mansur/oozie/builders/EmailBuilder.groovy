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

import groovy.xml.MarkupBuilder

/**
 * @author Ian Robertson
 * @since 8/01/13
 */
class EmailBuilder extends BaseBuilder {

    def buildXML(MarkupBuilder xml, HashMap<String, Object> action, HashMap<String, Object> common) {
        HashMap<String, Object> map = getMergedProperties(common, action)
        Map<String, String> actionAttributes = [name : map.get(NAME)];
        String cred = map.get(CRED);
        if (cred != null && cred.length() > 0) {
          actionAttributes+= ["cred": cred];
        }
        xml.action(actionAttributes) {
            'email'(xmlns:"uri:oozie:email-action:0.1") {
                addNode(map, xml, 'to', TO)
                addNode(map, xml, 'cc', CC)
                addNode(map, xml, 'subject', SUBJECT)
                addNode(map, xml, 'body', BODY)
            }
            addOkOrError(xml, map, "ok")
            addOkOrError(xml, map, "error")
        }
    }
}
