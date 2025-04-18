package com.klpdapp.klpd.Security;

import com.klpdapp.klpd.model.Login;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.klpdapp.klpd.Repository.LoginRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    @Autowired
    private LoginRepo LoginRepository;

     @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Login user = LoginRepository.findByEmail(email)
        .orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });
        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        LoginRepository.save(user);
        // Store user ID in session
        HttpSession session = request.getSession();
        session.setAttribute("userid", user.getId());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserType().toUpperCase())); // e.g. ROLE_ADMIN

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

    }
    
}
