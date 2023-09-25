package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
      Optional<User> userFromDb = userRepository.findById(id);
      return userFromDb.orElse(new User());
   }

   @Override
   @Transactional
   public void updateUser(long id, User user) {
      User userFrom_DB = userRepository.findById(id).get(); //user_from_DB из БД, кого хотим редактировать
      userFrom_DB.setUsername(user.getUsername());
      userFrom_DB.setLastName(user.getLastName());
      userFrom_DB.setAge(user.getAge());
      userFrom_DB.setEmail(user.getEmail());
      userFrom_DB.setRoles(user.getRoles());

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
      user.setRoles(user.getRoles());
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
   }
}
