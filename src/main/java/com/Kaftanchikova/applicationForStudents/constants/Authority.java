package com.Kaftanchikova.applicationForStudents.constants;

public class Authority {
    public static final String[] USER_AUTHORITIES = { "user:read" };
    public static final String[] APPLICANT_AUTHORITIES = { "user:read" };
    public static final String[] STUDENT_AUTHORITIES = { "user:read" };
    public static final String[] EDUCATOR_AUTHORITIES = { "user:read", "user:create" };
    public static final String[] ADMIN_AUTHORITIES = {"user:read", "user:create", "user:update"};
    public static final String[] SUPER_ADMIN_AUTHORITIES = {"user:read", "user:create", "user:update", "user:delete"};
}

