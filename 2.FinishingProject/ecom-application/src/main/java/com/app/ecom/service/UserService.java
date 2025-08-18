package com.app.ecom.service;

import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public User fetchUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        Address oldAddress = user.getAddress();
        if(oldAddress != null){
            oldAddress.setCity(updatedUser.getAddress().getCity());
            oldAddress.setStreet(updatedUser.getAddress().getStreet());
            oldAddress.setState(updatedUser.getAddress().getState());
            oldAddress.setCountry(updatedUser.getAddress().getCountry   ());
            oldAddress.setZipcode(updatedUser.getAddress().getZipcode());
            user.setAddress(oldAddress);
        }
        else {
            Address newAddress = updatedUser.getAddress();
            user.setAddress(newAddress);
        }
        userRepository.save(user);
        return true;
    }
}
