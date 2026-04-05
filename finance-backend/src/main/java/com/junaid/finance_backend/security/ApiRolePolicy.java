package com.junaid.finance_backend.security;

/**
 * Maps assignment roles to HTTP surface area. Spring {@code hasRole("X")} checks authority {@code ROLE_X};
 * these constants are the {@code X} part (aligned with {@link com.junaid.finance_backend.model.RoleType} names).
 * <p>
 * <b>VIEWER</b> — read dashboard aggregates only.<br>
 * <b>ANALYST</b> — dashboard + read/search financial records (no writes).<br>
 * <b>ADMIN</b> — users CRUD + full records CRUD + everything analysts/viewers can do.
 */
public final class ApiRolePolicy {

    public static final String VIEWER = "VIEWER";
    public static final String ANALYST = "ANALYST";
    public static final String ADMIN = "ADMIN";

    private ApiRolePolicy() {}
}
