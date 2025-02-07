package com.klpdapp.klpd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Services.CategoryService;
import com.klpdapp.klpd.dto.UserDto;
import com.klpdapp.klpd.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    CategoryService CategoryService;

    @Autowired
    UserRepo uRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showLogin(Model model, HttpSession session) {
        UserDto udto = new UserDto();
        model.addAttribute("dto", udto);
        CategoryService.addCategoriesToModel(model);
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "registration";
    }

    @PostMapping("/register")
    public String submitRegister(
            @ModelAttribute UserDto userDto,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
            // Encode the password
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());

            // Create a new User object
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(encodedPassword);
            user.setStatus("Active");

            // Save the user in the database
            uRepo.save(user);

            session.setAttribute("userid", user.getUserId());
            redirectAttributes.addFlashAttribute("message", "Registered Successfully and Logged In!");
            return "redirect:/profile";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Something Went Wrong!");
            return "redirect:/";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

}
