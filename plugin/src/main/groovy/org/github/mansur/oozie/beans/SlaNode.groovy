package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

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

  void buildXml(MarkupBuilder xml, CommonProperties common) {
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
