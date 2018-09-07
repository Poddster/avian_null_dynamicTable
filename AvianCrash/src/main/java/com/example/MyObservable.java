package com.example;

public class MyObservable {

    private Runnable mListener;

    MyObservable() {
    }

    public void register(Runnable listener) {
        System.out.println("register");
        mListener = listener;
    }

    public void notifyListener() {
        System.out.println("notifyListener");
        this.mListener.run();
    }
}
