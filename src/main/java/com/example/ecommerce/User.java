package com.example.ecommerce;

public class User {
    private final int user_id;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final String phone_no;

    User(int u_id, String f_name, String l_name, String email, String ph_no) {
        this.user_id = u_id;
        this.first_name = f_name;
        this.last_name = l_name;
        this.email = email;
        this.phone_no = ph_no;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone_no() {
        return this.phone_no;
    }
}
