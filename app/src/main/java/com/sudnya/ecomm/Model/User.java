package com.sudnya.ecomm.Model;

/**
 * @ Created by Dell on 21-Sep-17.
 */
public class User {

    private int id;
    private String name, email, contactno, password;

    public User(int id, String name, String email, String contactno) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactno = contactno;
    }

    public String getName() {
        return name;
    }

    public String getContactno() {
        return contactno;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}