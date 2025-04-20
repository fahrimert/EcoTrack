package com.example.mertsecurity.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum Role {

    ADMIN(
            "ADMIN",
            List.of("admin:write", "admin:put")
            ),
    USER("USER", List.of());

    private final String displayName;
    private final List<String> permissions;

    Role(String displayName, List<String> permissions) {
        this.displayName = displayName;
        this.permissions = permissions;
    }

    public String getDisplayName() { return displayName; }
    public List<String> getPermissions() { return permissions; }
}
