package org.github.mansur.oozie.beans

class HcatCredentialNode {
  String name;
  String hcatMetastoreUri;
  String hcatMetastorePrincipal

  String getType() { 'hcat' }
  Map <String, String> getProperties() {
    [ "hcat.metastore.uri": hcatMetastoreUri,
      "hcat.metastore.principal":  hcatMetastorePrincipal ]
  }
}
