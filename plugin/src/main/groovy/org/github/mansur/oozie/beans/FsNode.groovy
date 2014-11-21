package org.github.mansur.oozie.beans

import java.io.Serializable;

import groovy.xml.MarkupBuilder;
import static org.github.mansur.oozie.beans.NodeXmlUtils.*;

class FsChmodNode implements Serializable {
	private static final long serialVersionUID = 1L
  
	String path
	String permissions
	boolean dirFiles
	boolean recursive
  }
class FsChgrpNode implements Serializable {
	private static final long serialVersionUID = 1L
  
	String path
	String group
	boolean dirFiles
	boolean recursive
  }

class FsMoveNode implements Serializable {
	private static final long serialVersionUID = 1L
  
	String source
	String target
  }
  

final class FsNode extends ActionNode {
  private static final long serialVersionUID = 1L

  List<String> delete
  List<String> mkdir
  List<FsMoveNode> move
  List<FsChmodNode> chmod
  List<FsChgrpNode> chgrp
  List<String> touchz

  @Override
  public void buildXml(MarkupBuilder xml) {
    actionXml(xml){
      xml.'fs' {
        fsDelete(xml, delete)
		fsMkdir(xml, mkdir)
        move?.each {
          xml.'move'(source: it.source, target: it.target)
        }
        chmod?.each { chmod ->
          xml.'chmod'(path: chmod.path, permissions: chmod.permissions, 'dir-files': chmod.dirFiles) {
            if (chmod.recursive) {
              xml.'recursive'()
            }
          }
        }
		chgrp?.each { chgrp ->
			xml.'chgrp'(path: chgrp.path, group: chgrp.group, 'dir-files': chgrp.dirFiles) {
			  if (chgrp.recursive) {
				xml.'recursive'()
			  }
			}
		  }
        touchz?.each { xml.'touchz'(path: it) }
      }
    }
  }
}
