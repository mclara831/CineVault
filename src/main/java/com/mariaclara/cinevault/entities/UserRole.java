package com.mariaclara.cinevault.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_roles")
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;
    private String name;

    public UserRole() {
    }

    public UserRole(String name) {
        this.name = name;
    }

    public long getRoleId() {
        return roleId;
    }
    public String getName() {
        return name;
    }

    public enum Values {
        ADMIN(1L),
        BASIC(2);

        long roleId;

        Values(long id) {
            this.roleId = id;
        }

        public long getRoleId() {
            return roleId;
        }
    }
}
