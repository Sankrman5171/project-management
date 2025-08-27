package com.enggaid.projectportal.project_submission_portal.service;

import com.enggaid.projectportal.project_submission_portal.model.User;
import com.enggaid.projectportal.project_submission_portal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Save Users
    public void  saveUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username Already Exists.!!!");
        }

        if(user.getPassword() !=null && !user.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }


    //update users
    @Transactional
    public User updateUser(Long userId, User userDetails) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword()); // In production, hash the password!
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        }
        return null; // Or throw an exception if user is not found
    }

    //Delete User
    @Transactional
    public boolean deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
            return true; // Successful deletion
        }
        return false; // User not found
    }


    //Find User By Username
    public User findByUsername (String username){
        return (User) userRepository.findByUsername(username).orElse(null);
    }

    //Get list of users
    public List<User>getAllUsers(){
        return userRepository.findAll();
    }


    //Delete User By IDs
    public boolean deleteUserById(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }


    //Get user by IDs
    public Optional<User> getuserById(Long id){
        return  userRepository.findById(id);
    }


    //User updation
    public void updateUser(User user){
        if(userRepository.existsById(user.getId())){
            if(user.getPassword()!=null && !user.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(user);

        }
    }

    //user Deletion
    public void deletedById(Long id ){
        userRepository.deleteById(id);
    }

    //User Creation
    public void  createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
