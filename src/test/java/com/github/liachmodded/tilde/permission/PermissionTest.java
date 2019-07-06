package com.github.liachmodded.tilde.permission;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertSame;

final class PermissionTest {

    @Test
    void test() {
        //<editor-fold desc="Subject graph">
        Object owner = new Object();
        Object op = new Object();
        Object cmdBlock = new Object();
        Object mod = new Object();
        Object player = new Object();
        Object anything = new Object();

        // anything -> player -> mod -> op -> owner
        //          \-> cmdBlock ----/

        Graph<Object> subjectGraph = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .immutable()
                .addNode(owner)
                .addNode(op)
                .addNode(mod)
                .addNode(cmdBlock)
                .addNode(player)
                .addNode(anything)
                .putEdge(anything, player)
                .putEdge(anything, cmdBlock)
                .putEdge(player, mod)
                .putEdge(mod, op)
                .putEdge(cmdBlock, op)
                .putEdge(op, owner)
                .build();
        //</editor-fold>

        //<editor-fold desc="Key graph">
        Object allPerm = new Object();
        Object allCmd = new Object();
        Object lvl2Cmd = new Object();
        Object lvl0Cmd = new Object();
        // assume we have a forceload series
        Object forceLoadUse = new Object();
        Object forceLoadCheck = new Object();
        Object forceLoad = new Object();
        // and a data series
        Object dataModifyBlock = new Object();
        Object dataModify = new Object();
        Object data = new Object();
        // time
        Object time = new Object();
        // root
        Object rootCmd = new Object();

        Graph<Object> keyGraph = GraphBuilder.directed().allowsSelfLoops(false)
                .immutable()
                .addNode(allPerm)
                .addNode(allCmd)
                .addNode(lvl2Cmd)
                .addNode(lvl0Cmd)
                .addNode(forceLoadUse)
                .addNode(forceLoadCheck)
                .addNode(forceLoad)
                .addNode(dataModifyBlock)
                .addNode(dataModify)
                .addNode(data)
                .addNode(rootCmd)
                .putEdge(allPerm, allCmd)
                .putEdge(allCmd, lvl2Cmd)
                .putEdge(allCmd, forceLoadUse)
                .putEdge(allCmd, forceLoadCheck)
                .putEdge(allCmd, forceLoad)
                .putEdge(allCmd, dataModifyBlock)
                .putEdge(allCmd, dataModify)
                .putEdge(allCmd, data)
                .putEdge(allCmd, time)
                .putEdge(allCmd, rootCmd)
                .putEdge(lvl2Cmd, forceLoadCheck)
                .putEdge(lvl2Cmd, forceLoad)
                .putEdge(lvl2Cmd, dataModifyBlock)
                .putEdge(lvl2Cmd, dataModify)
                .putEdge(lvl2Cmd, data)
                .putEdge(lvl2Cmd, lvl0Cmd)
                .putEdge(lvl0Cmd, time)
                .putEdge(forceLoadUse, forceLoad)
                .putEdge(forceLoadCheck, forceLoad)
                .putEdge(forceLoad, rootCmd)
                .putEdge(dataModifyBlock, dataModify)
                .putEdge(dataModify, data)
                .putEdge(data, rootCmd)
                .putEdge(time, rootCmd)
                .build();
        //</editor-fold>

        BasicPermissionSystem<Object, Object> system = new BasicPermissionSystem<>(subjectGraph, keyGraph);

        system.set(owner, allPerm, PermissionValue.ALLOW);
        assertSame(PermissionValue.ALLOW, system.get(owner, dataModifyBlock));
        assertSame(PermissionValue.ALLOW, system.get(owner, rootCmd));
        assertSame(PermissionValue.NONE, system.getExact(owner, rootCmd));

        system.set(cmdBlock, lvl2Cmd, PermissionValue.ALLOW);
        system.set(player, lvl0Cmd, PermissionValue.ALLOW);

        assertSame(PermissionValue.ALLOW, system.get(op, dataModifyBlock));
        assertSame(PermissionValue.ALLOW, system.get(player, time));
        assertSame(PermissionValue.NONE, system.getExact(player, time));
        assertSame(PermissionValue.NONE, system.get(op, forceLoadUse));
    }
}
