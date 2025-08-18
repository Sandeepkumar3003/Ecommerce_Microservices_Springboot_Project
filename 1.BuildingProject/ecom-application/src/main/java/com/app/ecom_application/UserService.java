package com.app.ecom_application;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<User> userList = new ArrayList<>();
    private Long nextId = 0L;

    public List<User> fetchAllUsers() {
        return userList;
    }

    public User fetchUser(Long id) {
        for (User user : userList){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        user.setId(nextId++);
        userList.add(user);
    }
}
