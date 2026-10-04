package com.cth.job.api.controller;

import com.cth.job.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebViewController {

    private final UserService userService;

    @Autowired
    public WebViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("appName", userService.getAppName());
        model.addAttribute("authMode", userService.isLdapAuth() ? "LDAP Active Directory" : "Database Authentication");
        return "login";
    }

    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("appName", userService.getAppName());
        model.addAttribute("username", authentication != null ? authentication.getName() : "Guest");
        model.addAttribute("userRole", authentication != null && !authentication.getAuthorities().isEmpty() ? authentication.getAuthorities().iterator().next().getAuthority() : "USER");
        model.addAttribute("authMode", userService.isLdapAuth() ? "LDAP" : "DB");
        model.addAttribute("notificationsEnabled", userService.isNotificationsEnabled());
        return "index";
    }
}
