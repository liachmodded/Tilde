/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.permission.model;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableGraph;
import java.util.Set;

public class ChangeAwareGraph<N> implements MutableGraph<N> {

  private final MutableGraph<N> delegate;
  private boolean mutated = false;

  public ChangeAwareGraph(MutableGraph<N> delegate) {
    this.delegate = delegate;
  }

  public boolean hasMutated() {
    return mutated;
  }

  public void reset() {
    mutated = false;
  }

  //<editor-fold desc="Mutable graph delegates">
  @Override
  public boolean addNode(N node) {
    if (delegate.addNode(node)) {
      mutated = true;
      return true;
    }
    return false;
  }

  @Override
  public boolean putEdge(N nodeU, N nodeV) {
    if (delegate.putEdge(nodeU, nodeV)) {
      mutated = true;
      return true;
    }
    return false;
  }

  //    @Override
  //    public boolean putEdge(EndpointPair<N> endpoints) {
  //        if (delegate.putEdge(endpoints)) {
  //            mutated = true;
  //            return true;
  //        }
  //        return false;
  //    }

  @Override
  public boolean removeNode(Object node) {
    if (delegate.removeNode(node)) {
      mutated = true;
      return true;
    }
    return false;
  }

  @Override
  public boolean removeEdge(Object nodeU, Object nodeV) {
    if (delegate.removeEdge(nodeU, nodeV)) {
      mutated = true;
      return true;
    }
    return false;
  }

  //    @Override
  //    public boolean removeEdge(EndpointPair<N> endpoints) {
  //        if (delegate.removeEdge(endpoints)) {
  //            mutated = true;
  //            return true;
  //        }
  //        return false;
  //    }
  //</editor-fold>

  //<editor-fold desc="Regular graph delegates">
  @Override
  public Set<N> nodes() {
    return delegate.nodes();
  }

  @Override
  public Set<EndpointPair<N>> edges() {
    return delegate.edges();
  }

  @Override
  public boolean isDirected() {
    return delegate.isDirected();
  }

  @Override
  public boolean allowsSelfLoops() {
    return delegate.allowsSelfLoops();
  }

  @Override
  public ElementOrder<N> nodeOrder() {
    return delegate.nodeOrder();
  }

  @Override
  public Set<N> adjacentNodes(Object node) {
    return delegate.adjacentNodes(node);
  }

  @Override
  public Set<N> predecessors(Object node) {
    return delegate.predecessors(node);
  }

  @Override
  public Set<N> successors(Object node) {
    return delegate.successors(node);
  }

  //    @Override
  //    public Set<EndpointPair<N>> incidentEdges(N node) {
  //        return delegate.incidentEdges(node);
  //    }

  @Override
  public int degree(Object node) {
    return delegate.degree(node);
  }

  @Override
  public int inDegree(Object node) {
    return delegate.inDegree(node);
  }

  @Override
  public int outDegree(Object node) {
    return delegate.outDegree(node);
  }

  //    @Override
  //    public boolean hasEdgeConnecting(N nodeU, N nodeV) {
  //        return delegate.hasEdgeConnecting(nodeU, nodeV);
  //    }
  //
  //    @Override
  //    public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
  //        return delegate.hasEdgeConnecting(endpoints);
  //    }
  //</editor-fold>
}
