package com.example.timepickerios.picker;

@SuppressWarnings("ALL")
final class LoopRunnable implements Runnable {

    final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;
    }

    public final void run() {
        LoopListener listener = loopView.loopListener;
        int i = LoopView.getSelectItem(loopView);
        listener.onItemSelect(i);
    }
}
