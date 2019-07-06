/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.permission;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

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

        MutableGraph<Object> subjectGraph = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .build();
        subjectGraph.addNode(owner);
        subjectGraph.addNode(op);
        subjectGraph.addNode(mod);
        subjectGraph.addNode(cmdBlock);
        subjectGraph.addNode(player);
        subjectGraph.addNode(anything);
        subjectGraph.putEdge(anything, player);
        subjectGraph.putEdge(anything, cmdBlock);
        subjectGraph.putEdge(player, mod);
        subjectGraph.putEdge(mod, op);
        subjectGraph.putEdge(cmdBlock, op);
        subjectGraph.putEdge(op, owner);
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

        MutableGraph<Object> keyGraph = GraphBuilder.directed().allowsSelfLoops(false)
                .build();
        keyGraph.addNode(allPerm);
        keyGraph.addNode(allCmd);
        keyGraph.addNode(lvl2Cmd);
        keyGraph.addNode(lvl0Cmd);
        keyGraph.addNode(forceLoadUse);
        keyGraph.addNode(forceLoadCheck);
        keyGraph.addNode(forceLoad);
        keyGraph.addNode(dataModifyBlock);
        keyGraph.addNode(dataModify);
        keyGraph.addNode(data);
        keyGraph.addNode(rootCmd);
        keyGraph.putEdge(allPerm, allCmd);
        keyGraph.putEdge(allCmd, lvl2Cmd);
        keyGraph.putEdge(allCmd, forceLoadUse);
        keyGraph.putEdge(allCmd, forceLoadCheck);
        keyGraph.putEdge(allCmd, forceLoad);
        keyGraph.putEdge(allCmd, dataModifyBlock);
        keyGraph.putEdge(allCmd, dataModify);
        keyGraph.putEdge(allCmd, data);
        keyGraph.putEdge(allCmd, time);
        keyGraph.putEdge(allCmd, rootCmd);
        keyGraph.putEdge(lvl2Cmd, forceLoadCheck);
        keyGraph.putEdge(lvl2Cmd, forceLoad);
        keyGraph.putEdge(lvl2Cmd, dataModifyBlock);
        keyGraph.putEdge(lvl2Cmd, dataModify);
        keyGraph.putEdge(lvl2Cmd, data);
        keyGraph.putEdge(lvl2Cmd, lvl0Cmd);
        keyGraph.putEdge(lvl0Cmd, time);
        keyGraph.putEdge(forceLoadUse, forceLoad);
        keyGraph.putEdge(forceLoadCheck, forceLoad);
        keyGraph.putEdge(forceLoad, rootCmd);
        keyGraph.putEdge(dataModifyBlock, dataModify);
        keyGraph.putEdge(dataModify, data);
        keyGraph.putEdge(data, rootCmd);
        keyGraph.putEdge(time, rootCmd);
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
