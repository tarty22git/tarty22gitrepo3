package com.cth.job.api.controller;

import com.cth.job.service.entity.AppUser;
import com.cth.job.service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User maintenance, lock/unlock, password reset, and settings")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List all users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    public ResponseEntity<AppUser> createUser(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              @RequestParam("email") String email,
                                              @RequestParam("phone") String phone,
                                              @RequestParam("role") String role) {
        AppUser created = userService.createUser(username, password, email, phone, role);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{username}/lock")
    @Operation(summary = "Lock a user account")
    public ResponseEntity<String> lockUser(@PathVariable("username") String username) {
        userService.lockUser(username);
        return ResponseEntity.ok("User " + username + " locked.");
    }

    @PostMapping("/{username}/unlock")
    @Operation(summary = "Unlock a user account")
    public ResponseEntity<String> unlockUser(@PathVariable("username") String username) {
        userService.unlockUser(username);
        return ResponseEntity.ok("User " + username + " unlocked.");
    }

    @PostMapping("/{username}/reset-password")
    @Operation(summary = "Reset user password")
    public ResponseEntity<String> resetPassword(@PathVariable("username") String username,
                                                @RequestParam("newPassword") String newPassword) {
        userService.resetPassword(username, newPassword);
        return ResponseEntity.ok("Password reset successfully for " + username);
    }

    @PostMapping("/{username}/expire-password")
    @Operation(summary = "Set password expiration flag")
    public ResponseEntity<String> expirePassword(@PathVariable("username") String username,
                                                 @RequestParam("expired") boolean expired) {
        userService.togglePasswordExpired(username, expired);
        return ResponseEntity.ok("Password expiration set to " + expired + " for " + username);
    }

    @GetMapping("/settings")
    @Operation(summary = "Get platform settings")
    public ResponseEntity<Map<String, String>> getSettings() {
        return ResponseEntity.ok(Map.of(
                "appName", userService.getAppName(),
                "authMode", userService.isLdapAuth() ? "LDAP" : "DB",
                "notificationsEnabled", String.valueOf(userService.isNotificationsEnabled())
        ));
    }

    @PostMapping("/settings")
    @Operation(summary = "Update platform settings")
    public ResponseEntity<String> updateSettings(@RequestParam("appName") String appName,
                                                 @RequestParam("authMode") String authMode,
                                                 @RequestParam("notificationsEnabled") boolean notificationsEnabled) {
        userService.setAppName(appName);
        userService.setAuthMode(authMode);
        userService.setNotificationsEnabled(notificationsEnabled);
        return ResponseEntity.ok("Platform settings updated successfully.");
    }
}
