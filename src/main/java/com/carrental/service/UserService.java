package com.carrental.service;

import com.carrental.entity.User;

public interface UserService {
    User registerUser(User user);  // ✅ Matches the implementation exactly
    User getUserById(Long userId);
}
