package com.example;

public final class MyListener {

    public static final Runnable EVENT_HANDLER = MyListener::eventHandler;

    private MyListener() {
    }

    private static void eventHandler() {
        System.out.println("eventHandler called");
        AnotherStaticClass.someOtherStaticMethod();
    }

}
