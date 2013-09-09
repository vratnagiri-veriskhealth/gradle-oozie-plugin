package org.github.mansur.oozie.beans

class FsChmodNode implements Serializable {
  private static final long serialVersionUID = 1L

  String path
  String permissions
  boolean dirFiles
  boolean recursive


  Map<String, String> toMap() {
    def result = [ path: path, permissions: permissions ]
    if (dirFiles != null) {
      result += [dir_files: dirFiles ? 'true' : 'false']
    }
    return result
  }
}
