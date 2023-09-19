package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImp implements UserService {


   private final UserRepository userRepository;

   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UserServiceImp(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByUsername(username);
      if(user == null)
         throw new UsernameNotFoundException("User not found");
      return user;
   }

   public List<User> getAllUsers() {
      return userRepository.findAll();
   }

   @Override
   public User findByUsername(String name) {
      return userRepository.findByUsername(name);
   }
   @Override
   public User showUser(Long id) {
      User user = userRepository.findById(id).get();
      return user;
   }

   @Override
   @Transactional
   public void updateUser(long id, User user) {
      User user_from_DB = userRepository.findById(id).get(); //user_from_DB из БД, кого хотим редактировать
      user_from_DB.setUsername(user.getUsername());
      user_from_DB.setRoles((List<Role>) user.getAuthorities());
      if (!user.getPassword().equals(showUser(user.getId()).getPassword())) {
         user.setPassword(passwordEncoder.encode(user.getPassword()));
      }
      userRepository.save(user);
   }

   @Transactional
   @Override
   public void deleteUserById(Long id) {
      if (userRepository.findById(id).isPresent())
         userRepository.deleteById(id);
   }

   @Transactional
   @Override
   public void saveUser(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
   }
}
