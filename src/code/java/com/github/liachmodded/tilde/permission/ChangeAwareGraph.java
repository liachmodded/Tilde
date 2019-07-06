package com.github.liachmodded.tilde.permission;

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

    @Override
    public boolean putEdge(EndpointPair<N> endpoints) {
        if (delegate.putEdge(endpoints)) {
            mutated = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeNode(N node) {
        if (delegate.removeNode(node)) {
            mutated = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEdge(N nodeU, N nodeV) {
        if (delegate.removeEdge(nodeU, nodeV)) {
            mutated = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEdge(EndpointPair<N> endpoints) {
        if (delegate.removeEdge(endpoints)) {
            mutated = true;
            return true;
        }
        return false;
    }
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
    public Set<N> adjacentNodes(N node) {
        return delegate.adjacentNodes(node);
    }

    @Override
    public Set<N> predecessors(N node) {
        return delegate.predecessors(node);
    }

    @Override
    public Set<N> successors(N node) {
        return delegate.successors(node);
    }

    @Override
    public Set<EndpointPair<N>> incidentEdges(N node) {
        return delegate.incidentEdges(node);
    }

    @Override
    public int degree(N node) {
        return delegate.degree(node);
    }

    @Override
    public int inDegree(N node) {
        return delegate.inDegree(node);
    }

    @Override
    public int outDegree(N node) {
        return delegate.outDegree(node);
    }

    @Override
    public boolean hasEdgeConnecting(N nodeU, N nodeV) {
        return delegate.hasEdgeConnecting(nodeU, nodeV);
    }

    @Override
    public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
        return delegate.hasEdgeConnecting(endpoints);
    }
    //</editor-fold>
}
