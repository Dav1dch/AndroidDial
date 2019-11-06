package com.tuffy.dial;

import androidx.annotation.NonNull;

/**
 * @author david
 */
public class MyContacts {
    private String name;
    private String num;

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @NonNull
    @Override
    public String toString() {
        return "MyContacts{" +
                "name='" + name + '\'' +
                ", phone='" + num + '\'' +
                '}';
    }
}
