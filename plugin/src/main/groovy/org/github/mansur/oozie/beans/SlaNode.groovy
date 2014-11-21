package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;

enum SlaEvents {
	START_MISS("start_miss"),
	END_MISS("end_miss"),
	DURATION_MISS("duration_miss")
  
	final String name;
  
	private SlaEvents(String name) {
	  this.name = name;
	}
  
	private final static long serialVersionUID = 1L;
  }

class SlaNode extends XmlCapable {
  private final static long serialVersionUID = 1L;

  String nominalTime = '${nominal_time}'
  String shouldStart
  String shouldEnd
  String maxDuration
  List<SlaEvents> alertEvents
  String alertContact
  String notificationMessage
  String upstreamApps

  void buildXml(MarkupBuilder xml) {
    xml.info(xmlns: 'uri:oozie:sla:0.2') {
      xml.'nominal-time'(nominalTime)
      addNode(xml, 'should-start', shouldStart)
      xml.'should-end'(shouldEnd)
      addNode(xml, 'max-duration', maxDuration)
      if (alertEvents != null && !alertEvents.isEmpty()) {
        xml.'alert-events'((alertEvents*.name).join(","))
      }
      addNode(xml, 'alert-contact', alertContact)
      addNode(xml, 'notification-msg', notificationMessage)
      addNode(xml, 'upstream-apps', upstreamApps)
    }
  }
}
