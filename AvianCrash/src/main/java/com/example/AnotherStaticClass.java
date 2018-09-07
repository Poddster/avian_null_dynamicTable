package com.example;

public final class AnotherStaticClass {

    // This needs to exist
    private static final Runnable someLambda = () -> {
    };

    private AnotherStaticClass() {
    }

    public static void someOtherStaticMethod() {
        System.out.println("someOtherStaticMethod");
    }

}
