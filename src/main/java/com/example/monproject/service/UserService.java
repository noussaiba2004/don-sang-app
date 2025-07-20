package com.example.monproject.service;

import com.example.monproject.dto.UserDto;
import com.example.monproject.model.Centre;
import com.example.monproject.model.User;
import com.example.monproject.repository.CentreRepository;
import com.example.monproject.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final CentreRepository centreRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository,CentreRepository centreRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.centreRepository = centreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        System.out.println("Tentative de connexion avec l'email : " + email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            System.out.println("Utilisateur non trouvé : " + email);
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }

        System.out.println("Utilisateur trouvé : " + user.getEmail() + " - Rôle : " + user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
        }
        return user;
    }
    public void registerCentre(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("CENTRE"); // ou comme tu gères les rôles

        // Recherche du centre correspondant à l’email
        Centre centre = centreRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Centre non trouvé pour l'email : " + userDto.getEmail()));

        if (centre == null) {
            throw new RuntimeException("Aucun centre trouvé avec cet email.");
        }

        // Lien entre les deux
        user.setCentre(centre);
        centre.setUser(user);

        userRepository.save(user);
    }

    public List<String> getEmailsDesDonneurs() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().contains("DONNEUR"))
                .map(User::getEmail)
                .toList();
    }


}

