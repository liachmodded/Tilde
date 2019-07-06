package com.github.liachmodded.tilde.permission;

import com.google.common.graph.Graph;
import com.google.common.graph.MutableGraph;

import java.util.HashMap;
import java.util.Map;

public class BasicPermissionSystem<S, K> extends AbstractPermissionSystem<S, K> implements MutablePermissionSystem<S, K> {

    private final Map<PermissionIndex<S, K>, PermissionValue> map = makeMap();
    private final ChangeAwareGraph<S> subjectRelations;
    private final ChangeAwareGraph<K> keyRelations;

    public BasicPermissionSystem(MutableGraph<S> subjectRelations, MutableGraph<K> keyRelations) {
        this(new ChangeAwareGraph<>(subjectRelations), new ChangeAwareGraph<>(keyRelations));
    }

    public BasicPermissionSystem(Graph<S> subjectRelations, Graph<K> keyRelations) {
        super(subjectRelations, keyRelations);
        this.subjectRelations = null;
        this.keyRelations = null;
    }

    private BasicPermissionSystem(ChangeAwareGraph<S> subjectRelations, ChangeAwareGraph<K> keyRelations) {
        super(subjectRelations, keyRelations);
        this.subjectRelations = subjectRelations;
        this.keyRelations = keyRelations;
    }

    protected <A, B> Map<A, B> makeMap() {
        return new HashMap<>();
    }

    @Override
    public void set(S subject, K key, PermissionValue value) {
        final PermissionIndex<S, K> index = new PermissionIndex<>(subject, key);
        if (value == PermissionValue.NONE) {
            map.remove(index);
        } else {
            map.put(index, value);
        }
    }

    @Override
    public void update(S subject, K key, PermissionUpdater operator) {
        final PermissionIndex<S, K> index = new PermissionIndex<>(subject, key);
        final PermissionValue exactValue = getExact(index);
        if (exactValue != PermissionValue.NONE) {
            final PermissionValue updated = operator.update(exactValue, true);
            if (updated == PermissionValue.NONE) {
                map.remove(index);
            } else {
                map.put(index, updated);
            }
            return;
        }

        final PermissionValue updated = operator.update(getAncestor(index), false);
        if (updated == PermissionValue.NONE) {
            map.remove(index);
        } else {
            map.put(index, updated);
        }
    }

    @Override
    protected PermissionValue getExact(PermissionIndex<S, K> index) {
        return map.getOrDefault(index, PermissionValue.NONE);
    }

    class PermDef {
        int subjectParentVersion;
        int keyParentVersion;

        CachedPermissionValue value;
    }

    enum CachedPermissionValue {
        ALLOW(PermissionValue.ALLOW, true),
        DENY(PermissionValue.DENY, true),
        PARENT_ALLOW(PermissionValue.ALLOW, false),
        PARENT_DENY(PermissionValue.DENY, false),
        NONE(PermissionValue.NONE, false);

        final boolean explicit;
        final PermissionValue value;

        CachedPermissionValue(PermissionValue value, boolean explicit) {
            this.value = value;
            this.explicit = explicit;
        }
    }

}