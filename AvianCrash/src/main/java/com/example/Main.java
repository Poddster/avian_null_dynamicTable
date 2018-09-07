package com.example;


public final class Main {

    private Main() {
    }

    public static void main(String[] args) {

        Thread.currentThread().setName("MainThread");

        MyObservable myObservable = new MyObservable();

        // The problem only happens if delayedEvent is a anonymous or named class
        // It does *not* happen if it is a lambda.
        DelayedEvent delayedEvent = new DelayedEvent(myObservable);
        Thread delayedEventThread = new Thread(delayedEvent, "DelayedEvent");

        // This listener has to register after the delayedEventThread is started
        // If you move the call to start AFTER the register the problem won't happen.
        delayedEventThread.start();
        myObservable.register(MyListener.EVENT_HANDLER);

        System.out.println("waiting for crash...");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new AssertionError(
                "Expected Avian JVM to crash when calling" +
                        " AnotherStaticClass.someOtherStaticMethod ");
    }

}

class DelayedEvent implements Runnable {
    private final MyObservable observable;

    DelayedEvent(MyObservable observable) {
        this.observable = observable;
    }

    @Override
    public void run() {
        System.out.println("DelayedEvent thread started");
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        observable.notifyListener();
    }
}
