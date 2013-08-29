package org.github.mansur.oozie.beans

import java.util.regex.Pattern;

class NameChecker {
  private final static Pattern namePattern = Pattern.compile("[a-zA-Z_]([\\-_a-zA-Z0-9]){0,39}");
  static String verify(String name) {
    if (! namePattern.matcher(name).matches()) {
      throw new IllegalArgumentException("Name '${name}' is not a valid oozie identifier")
    }
    return name;
  }
}
