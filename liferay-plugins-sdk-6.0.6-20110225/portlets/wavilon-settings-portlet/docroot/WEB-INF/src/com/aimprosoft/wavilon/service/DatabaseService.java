package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.User;

import java.util.List;

//todo will be implemented
public interface DatabaseService {

    void addUser(User user);

    User getUser(Long id);
    List<User> getAllUsers();

    void removeUser(User user);
    void removeUser(Long id);

    void updateUser(User user);



}
