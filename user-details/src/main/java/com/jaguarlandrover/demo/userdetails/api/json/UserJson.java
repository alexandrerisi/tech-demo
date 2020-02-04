package com.jaguarlandrover.demo.userdetails.api.json;

import com.jaguarlandrover.demo.userdetails.domain.User;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;

@Data
public class UserJson {

    private String username;
    private String password;
    private Collection<String> permissions;

    public static UserJson userToJson(User user) {
        var json = new UserJson();
        json.setUsername(user.getUsername());
        json.setPassword(user.getPassword());
        json.setPermissions(new HashSet<>());
        user.getPermissions()
                .forEach(permission -> json.getPermissions().add(permission.getDescription()));
        return json;
    }
}
