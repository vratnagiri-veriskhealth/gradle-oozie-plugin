package org.github.mansur.oozie.builders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Muhammad Ashraf
 * @since 7/23/13
 */
public class DirectedGraph {

    private final List<Node> nodes;

    public DirectedGraph() {
        nodes = new ArrayList<Node>();
    }

    public void addNode(final Node node) {
        nodes.add(node);
    }

    public void addNodes(final Node... nodes) {
        Collections.addAll(this.nodes, nodes);
    }


    /**
     * Topoligically sort the nodes, so that any node only points to nodes later in the list.
     * Note that this algorithm is destructive to the graph; when done, edges will be gone.
     * @return a topoligically sorted list of the nodes
     */
    public List<Node> tSort() {
      // Kahn's algorithm; see http://en.wikipedia.org/wiki/Topological_sorting#Algorithms
      List<Node> result = new ArrayList<>();
      if (nodes.isEmpty()) { return result; }
      Set<Node> toProcess = new HashSet<>();
      toProcess.add(findHead());
      while(!toProcess.isEmpty()) {
        Node n = firstElement(toProcess);
        result.add(n);
        toProcess.remove(n);
        for (Edge e: new HashSet<>(n.outEdges)) { // avoid concurrent modification exception
          e.remove();
          if (e.to.inEdges.isEmpty()) {
            toProcess.add(e.to);
          }
        }
      }
      for (Node node: nodes) {
        if (! node.outEdges.isEmpty()) {
          throw new IllegalStateException("the flow from " + node.name + " to " + firstElement(node.outEdges).to.name
              + " is part of a cycle");
        }
      }
      return result;
    }

    private <T> T firstElement(Iterable<T> elements) {
      return elements.iterator().next();
    }

    Node findHead() {
        final LinkedHashSet<Node> nodes = new LinkedHashSet<Node>();
        for (final Node n : this.nodes) {
            if (n.inEdges.size() == 0) {
                nodes.add(n);
            }
        }

        if (nodes.size() > 1) {
            throw new IllegalStateException("Multiple Starting nodes Founds!" + nodes.toString());
        } else if (nodes.isEmpty()) {
            throw new IllegalStateException("No Starting node Found!");
        }

        return nodes.iterator().next();
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      for (Node node: nodes) {
        builder.append("{ ").append(node.name).append("->(");
        for (Edge edge: node.outEdges) {
          builder.append(edge.to.name).append(",");
        }
        builder.append(")}, ");
      }
      return builder.toString();
    }

    public static class Node {
        private final String name;
        private final LinkedHashSet<Edge> inEdges = new LinkedHashSet<Edge>();
        private final LinkedHashSet<Edge> outEdges = new LinkedHashSet<Edge>();

        public Node(final String name) {
            this.name = name;
        }

        public Node addEdge(final Node node) {
            final Edge e = new Edge(this, node);
            outEdges.add(e);
            node.inEdges.add(e);
            return this;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Node node = (Node) o;

            return !(name != null ? !name.equals(node.name) : node.name != null);
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

    public static class Edge {
        public final Node from;
        public final Node to;

        public Edge(final Node from, final Node to) {
            this.from = from;
            this.to = to;
        }

        private void remove() {
          from.outEdges.remove(this);
          to.inEdges.remove(this);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) return false;
            if (obj.getClass() != this.getClass()) return false;
            final Edge e = (Edge) obj;
            return e.from == from && e.to == to;
        }
    }

}
