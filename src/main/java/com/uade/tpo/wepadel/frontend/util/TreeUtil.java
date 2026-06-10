package com.uade.tpo.wepadel.frontend.util;

import javax.swing.JTree;

public final class TreeUtil {

    private TreeUtil() {
    }

    public static void expandirTodo(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}
