/*
 * Copyright 2013. Muhammad Ashraf
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.github.mansur.oozie;

import org.github.mansur.oozie.builders.DirectedGraph;
import org.github.mansur.oozie.builders.DirectedGraph.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Muhammad Ashraf
 * @since 7/23/13
 */
public class DirectedGraphTest {

    @Test
    public void testSort() throws Exception {
        final DirectedGraph.Node node1 = new DirectedGraph.Node("node1");
        final DirectedGraph.Node node2 = new DirectedGraph.Node("node2");
        final DirectedGraph.Node node3 = new DirectedGraph.Node("node3");
        final DirectedGraph.Node node4 = new DirectedGraph.Node("node4");
        node1.addEdge(node2);
        node2.addEdge(node3);
        node3.addEdge(node4);

        final List<DirectedGraph.Node> sortedGraph = makeGraph(node2, node1, node3, node4).tSort();
        assertThat(sortedGraph).containsExactly(node1, node2, node3, node4);
    }

    @Test
    public void testOneNodeFlow() throws Exception {
        final DirectedGraph.Node node1 = new DirectedGraph.Node("node1");

        final List<DirectedGraph.Node> sortedGraph = makeGraph(node1).tSort();
        assertThat(sortedGraph).containsExactly(node1);

    }

    @Test
    public void testNonCyclic() {
      List<DirectedGraph.Node> nodes = new ArrayList<>();
      for (int i = 0; i < 4; i++) {
        DirectedGraph.Node node = new DirectedGraph.Node("node" + i);
        for (Node n : nodes) {
          n.addEdge(node);
        }
        nodes.add(node);
      }
      List<Node> sorted = makeGraph(nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(3)).tSort();
      assertEquals(nodes, sorted);
    }

    @Test
    public void testDiamondDag() {
      Node start = new Node("root");
      Node left = new Node("left");
      Node right = new Node("right");
      Node end = new Node("end");
      start.addEdge(left);
      start.addEdge(right);
      left.addEdge(end);
      right.addEdge(end);
      List<Node> sorted = makeGraph(start, left, right, end).tSort();
      assertEquals(4, sorted.size());
      assertEquals(start, sorted.get(0));
      assertEquals(end, sorted.get(3));
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleHeads() throws Exception {
        final DirectedGraph.Node node1 = new DirectedGraph.Node("node1");
        final DirectedGraph.Node node2 = new DirectedGraph.Node("node2");
        final DirectedGraph.Node node3 = new DirectedGraph.Node("node3");
        final DirectedGraph.Node node4 = new DirectedGraph.Node("node4");
        node2.addEdge(node3);
        node3.addEdge(node4);

        makeGraph(node2, node1, node3, node4).tSort();
    }

    @Test(expected = IllegalStateException.class)
    public void testCyclicGraph() throws Exception {
        final DirectedGraph.Node node1 = new DirectedGraph.Node("node1");
        final DirectedGraph.Node node2 = new DirectedGraph.Node("node2");
        final DirectedGraph.Node node3 = new DirectedGraph.Node("node3");
        node1.addEdge(node2);
        node2.addEdge(node3);
        node3.addEdge(node2);

        makeGraph(node2, node1, node3).tSort();
    }

    private static DirectedGraph makeGraph(DirectedGraph.Node... nodes) {
      DirectedGraph graph = new DirectedGraph();
      for(DirectedGraph.Node node: nodes) {
        graph.addNode(node);
      }
      return graph;
    }
}
