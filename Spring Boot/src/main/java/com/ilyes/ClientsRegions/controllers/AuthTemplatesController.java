package com.ilyes.ClientsRegions.controllers;


import com.ilyes.ClientsRegions.auth.AuthenticationRequest;
import com.ilyes.ClientsRegions.auth.RegisterRequest;
import com.ilyes.ClientsRegions.config.LogoutService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthTemplatesController {

    @Autowired
    LogoutService logoutService;
    @GetMapping("/login")
    public String home(ModelMap modelMap) {
        modelMap.addAttribute("AuthenticationRequest", new AuthenticationRequest());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(ModelMap modelMap, @ModelAttribute("error") String error) {
        try {
            modelMap.addAttribute("registerRequest", new RegisterRequest());
            if (error.startsWith("User")) modelMap.addAttribute("error", error);
            return "/register";
        }catch (Exception e){
            return "/login";
        }
    }

    @GetMapping("/logout")
    public String logout(ModelMap modelMap,
                         @ModelAttribute("error") String error,
                         HttpServletRequest request,
                         HttpServletResponse response) {

        // Get the current authentication from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Call the LogoutService's logout method to invalidate the token and clear the security context
        logoutService.logout(request, response, authentication);

        // Delete the "token" cookie from the response
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // Redirect the user to the login page
        return "redirect:/login";
    }

}
