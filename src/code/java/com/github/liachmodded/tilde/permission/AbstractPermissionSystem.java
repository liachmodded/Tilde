package com.github.liachmodded.tilde.permission;

import com.google.common.graph.Graph;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public abstract class AbstractPermissionSystem<S, K> implements PermissionSystem<S, K> {

    private final Graph<S> subjectRelations;
    private final Graph<K> keyRelations;

    protected AbstractPermissionSystem(Graph<S> subjectRelations, Graph<K> keyRelations) {
        this.subjectRelations = subjectRelations;
        this.keyRelations = keyRelations;
    }

    @Override
    public Graph<S> getSubjectRelations() {
        return subjectRelations;
    }

    @Override
    public Graph<K> getKeyRelations() {
        return keyRelations;
    }

    @Override
    public PermissionValue get(S subject, K key) {
        final PermissionIndex<S, K> index = new PermissionIndex<>(subject, key);
        final PermissionValue single = getExact(index);
        if (single != PermissionValue.NONE)
            return single;

        // Explore x/y axes
        return getAncestor(index);
    }

    protected PermissionValue getAncestor(PermissionIndex<S, K> index) {
        final Queue<PermissionIndex<S, K>> queue = new ArrayDeque<>();
        queue.add(index);
        final Set<PermissionIndex<S, K>> explored = new HashSet<>();
        explored.add(index);

        boolean gotYes = false;

        // very inefficient! need to investigate link cut tree or so for performance concerns.
        while (!queue.isEmpty()) {
            final PermissionIndex<S, K> current = queue.remove();
            final S currentSubject = current.getSubject();
            final K currentKey = current.getKey();

            for (S parentSubject : getSubjectRelations().predecessors(currentSubject)) {
                final PermissionIndex<S, K> next = new PermissionIndex<>(parentSubject, currentKey);
                final PermissionValue val = getExact(next);
                if (val == PermissionValue.DENY)
                    return PermissionValue.DENY;
                if (val == PermissionValue.ALLOW)
                    gotYes = true;

                if (val == PermissionValue.NONE && explored.add(next)) {
                    queue.add(next);
                }
            }

            for (K parentKey : getKeyRelations().predecessors(currentKey)) {
                final PermissionIndex<S, K> next = new PermissionIndex<>(currentSubject, parentKey);
                final PermissionValue val = getExact(next);
                if (val == PermissionValue.DENY)
                    return PermissionValue.DENY;
                if (val == PermissionValue.ALLOW)
                    gotYes = true;

                if (val == PermissionValue.NONE && explored.add(next)) {
                    queue.add(next);
                }
            }
        }

        return gotYes ? PermissionValue.ALLOW : PermissionValue.NONE;
    }

    @Override
    public PermissionValue getExact(S subject, K key) {
        final PermissionIndex<S, K> index = new PermissionIndex<>(subject, key);
        return getExact(index);
    }

    protected abstract PermissionValue getExact(PermissionIndex<S, K> index);
}
