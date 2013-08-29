package org.github.mansur.oozie.beans

class HcatCredentialNode implements CredentialNode {
  private static final long serialVersionUID = 1L

  String name;
  String metastoreUri;
  String metastorePrincipal

  String getType() { 'hcat' }
  Map <String, String> getProperties() {
    [ "hcat.metastore.uri": metastoreUri,
      "hcat.metastore.principal":  metastorePrincipal ]
  }
}
