package com.educavalieri.dscatolog.dto;

public class UserInsertDto extends UserDTO{

    private String password;

    private UserInsertDto(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
