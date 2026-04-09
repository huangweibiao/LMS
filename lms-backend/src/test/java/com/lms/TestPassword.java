package com.lms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Test hash from schema.sql
        String schemaHash = "$2a$10$EqKcp1WFKVQISheB5.yvDOImC.CxHGL.7eh6BWjqGR5YQmvQ9Vm3S";

        // Generate fresh hash for "admin123"
        String freshHash = encoder.encode("admin123");

        System.out.println("Schema hash:   " + schemaHash);
        System.out.println("Fresh hash:    " + freshHash);
        System.out.println("Schema matches: " + encoder.matches("admin123", schemaHash));
        System.out.println("Fresh matches:  " + encoder.matches("admin123", freshHash));
    }
}