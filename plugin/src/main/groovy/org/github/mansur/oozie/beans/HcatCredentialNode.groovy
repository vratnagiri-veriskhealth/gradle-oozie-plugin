package org.github.mansur.oozie.beans

class HcatCredentialNode {
  String name;
  String metastoreUri;
  String metastorePrincipal

  String getType() { 'hcat' }
  Map <String, String> getProperties() {
    [ "hcat.metastore.uri": metastoreUri,
      "hcat.metastore.principal":  metastorePrincipal ]
  }
}
