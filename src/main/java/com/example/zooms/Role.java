package com.example.zooms;

public class Role {
    private int roleId;
    private String roleName;
    private double salary;

    public Role(int roleId, String roleName, double salary) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.salary = salary;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
