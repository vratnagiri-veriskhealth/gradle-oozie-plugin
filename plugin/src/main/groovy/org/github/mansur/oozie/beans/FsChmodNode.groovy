package org.github.mansur.oozie.beans

class FsChmodNode implements Serializable {
  private static final long serialVersionUID = 1L

  String path
  String permissions
  boolean dirFiles
  boolean recursive
}
