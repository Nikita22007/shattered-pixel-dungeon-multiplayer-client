package com.nikita22007.multiplayer.utils.text;

import java.util.Objects;

public class LocalizedKey {

    private final String ownerClass;
    private final String name;

    public LocalizedKey(String ownerClass, String name) {
        this.ownerClass = ownerClass;
        this.name = name;
    }

    public String ownerClass() {
        return ownerClass;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalizedKey)) {
            return false;
        }
        LocalizedKey other = (LocalizedKey) obj;
        return Objects.equals(ownerClass, other.ownerClass)
                && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerClass, name);
    }
}
