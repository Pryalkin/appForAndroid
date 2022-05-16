package com.Kaftanchikova.applicationForStudents.enumeration;

import static com.Kaftanchikova.applicationForStudents.constants.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_APPLICANT(APPLICANT_AUTHORITIES),
    ROLE_STUDENT(STUDENT_AUTHORITIES),
    ROLE_EDUCATOR(EDUCATOR_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
