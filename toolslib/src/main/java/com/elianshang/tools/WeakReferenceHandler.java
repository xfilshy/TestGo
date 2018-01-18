package com.elianshang.tools;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 虚引用持有对象的handler，降低代码不当的内存泄露问题
 * <p>
 * HandleMessage时请不要直接引用其他对象，而是通过Hook操作
 */
public class WeakReferenceHandler<Hook> extends Handler {

    private WeakReference<Hook> mWeakReference;

    public WeakReferenceHandler(Hook hook, Looper looper) {
        super(looper);
        mWeakReference = new WeakReference<Hook>(hook);
    }

    public WeakReferenceHandler(Hook hook) {
        mWeakReference = new WeakReference<Hook>(hook);
    }

    @Override
    public final void handleMessage(Message msg) {
        Hook hook = mWeakReference.get();
        if (hook != null) {
            HandleMessage(hook, msg);
        }
    }

    public void HandleMessage(Hook hook, Message msg) {

    }

    public void post(final WeakReferenceHandlerRunnalbe<Hook> runnalbe) {

        post(new Runnable() {
            @Override
            public void run() {
                Hook hook = mWeakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        });
    }

    public void postAtFrontOfQueue(final WeakReferenceHandlerRunnalbe<Hook> runnalbe) {

        postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                Hook hook = mWeakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        });
    }

    public void postDelayed(final WeakReferenceHandlerRunnalbe<Hook> runnalbe, long delayMillis) {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                Hook hook = mWeakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        }, delayMillis);
    }

    public void postAtTime(final WeakReferenceHandlerRunnalbe<Hook> runnalbe, long uptimeMillis) {

        postAtTime(new Runnable() {
            @Override
            public void run() {
                Hook hook = mWeakReference.get();
                if (hook != null) {
                    runnalbe.run(hook);
                }
            }
        }, uptimeMillis);
    }

    public interface WeakReferenceHandlerRunnalbe<Hook> {
        void run(Hook hook);
    }
}
