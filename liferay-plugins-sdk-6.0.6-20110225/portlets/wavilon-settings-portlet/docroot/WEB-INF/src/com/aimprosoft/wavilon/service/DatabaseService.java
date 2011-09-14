package com.aimprosoft.wavilon.service;

import com.aimprosoft.wavilon.model.User;

import java.io.IOException;
import java.util.List;

//todo move to spring
public interface DatabaseService {

    void addUser(User user) throws IOException;

    User getUser(String id) throws IOException;

    List<User> getAllUsers() throws IOException;

    void removeUser(User user) throws IOException;

    void removeUser(String id) throws IOException;

    void updateUser(User user) throws IOException;
}
