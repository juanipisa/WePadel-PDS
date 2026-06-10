package com.uade.tpo.wepadel.frontend.util;

import java.awt.Component;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

public final class SwingExecutor {

    private SwingExecutor() {
    }

    public static <T> void run(Component parent, Callable<T> task, Consumer<T> onSuccess) {
        run(parent, task, onSuccess, null);
    }

    public static <T> void run(Component parent, Callable<T> task, Consumer<T> onSuccess, Runnable onFinally) {
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    if (onSuccess != null) {
                        onSuccess.accept(result);
                    }
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    Dialogs.error(parent, cause instanceof Exception e ? e : new Exception(cause.getMessage()));
                } finally {
                    if (onFinally != null) {
                        onFinally.run();
                    }
                }
            }
        }.execute();
    }

    public static void runVoid(Component parent, Runnable task, Runnable onSuccess) {
        run(parent, () -> {
            task.run();
            return null;
        }, r -> {
            if (onSuccess != null) {
                onSuccess.run();
            }
        });
    }
}
