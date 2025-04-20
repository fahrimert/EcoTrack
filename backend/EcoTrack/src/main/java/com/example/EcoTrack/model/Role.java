package com.example.EcoTrack.model;

import java.util.List;

public enum Role {

    WORKER(
            "WORKER",
            List.of("worker:write", "worker:put","worker:get","worker:delete")
            ),
    SUPERVISOR("SUPERVİSOR",
            List.of("supervisor:write", "supervisor:put","supervisor:get","supervisor:delete")
    ),
    MANAGER("MANAGER",
               List.of("manager:write", "manager:put","manager:get","manager:delete")
    );

    private final String displayName;
    private final List<String> permissions;

    Role(String displayName, List<String> permissions) {
        this.displayName = displayName;
        this.permissions = permissions;
    }

    public String getDisplayName() { return displayName; }
    public List<String> getPermissions() { return permissions; }
}
