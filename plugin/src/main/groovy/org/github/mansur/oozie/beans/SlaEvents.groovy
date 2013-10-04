package org.github.mansur.oozie.beans

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
