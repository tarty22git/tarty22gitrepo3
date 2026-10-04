package com.cth.job.service.service;

import com.cth.job.service.entity.AppUser;
import com.cth.job.service.entity.SystemConfig;
import com.cth.job.service.repository.AppUserRepository;
import com.cth.job.service.repository.SystemConfigRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final AppUserRepository userRepository;
    private final SystemConfigRepository configRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AppUserRepository userRepository, SystemConfigRepository configRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.configRepository = configRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initDefaultUsersAndConfig() {
        if (!configRepository.existsById("app_name")) {
            configRepository.save(new SystemConfig("app_name", "GlobalPay Platform"));
        }
        if (!configRepository.existsById("auth_mode")) {
            configRepository.save(new SystemConfig("auth_mode", "DB"));
        }
        if (!configRepository.existsById("notifications_enabled")) {
            configRepository.save(new SystemConfig("notifications_enabled", "false"));
        }

        createDefaultUserIfAbsent("admin", "admin", "admin@cth.job", "+1000000000", "ADMIN");
        createDefaultUserIfAbsent("maker", "maker", "maker@cth.job", "+1000000001", "MAKER");
        createDefaultUserIfAbsent("checker", "checker", "checker@cth.job", "+1000000002", "CHECKER");
    }

    private void createDefaultUserIfAbsent(String username, String rawPassword, String email, String phone, String role) {
        if (!userRepository.existsByUsername(username)) {
            AppUser user = new AppUser(username, passwordEncoder.encode(rawPassword), email, phone, role);
            userRepository.save(user);
            log.info("Created default user: {} with role: {}", username, role);
        }
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Transactional
    public AppUser createUser(String username, String rawPassword, String email, String phone, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        AppUser user = new AppUser(username, passwordEncoder.encode(rawPassword), email, phone, role);
        AppUser saved = userRepository.save(user);

        if (isNotificationsEnabled()) {
            sendNotification(saved, "Account created successfully.");
        }
        return saved;
    }

    @Transactional
    public void lockUser(String username) {
        AppUser user = getUserByUsername(username);
        user.setAccountLocked(true);
        userRepository.save(user);
    }

    @Transactional
    public void unlockUser(String username) {
        AppUser user = getUserByUsername(username);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    @Transactional
    public void resetPassword(String username, String newRawPassword) {
        AppUser user = getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(newRawPassword));
        user.setPasswordExpired(false);
        userRepository.save(user);
    }

    @Transactional
    public void togglePasswordExpired(String username, boolean expired) {
        AppUser user = getUserByUsername(username);
        user.setPasswordExpired(expired);
        userRepository.save(user);
    }

    public String getConfigValue(String key, String defaultValue) {
        return configRepository.findById(key)
                .map(SystemConfig::getValue)
                .orElse(defaultValue);
    }

    @Transactional
    public void setConfigValue(String key, String value) {
        configRepository.save(new SystemConfig(key, value));
    }

    public boolean isLdapAuth() {
        return "LDAP".equalsIgnoreCase(getConfigValue("auth_mode", "DB"));
    }

    @Transactional
    public void setAuthMode(String mode) {
        setConfigValue("auth_mode", mode);
    }

    public boolean isNotificationsEnabled() {
        return Boolean.parseBoolean(getConfigValue("notifications_enabled", "false"));
    }

    @Transactional
    public void setNotificationsEnabled(boolean enabled) {
        setConfigValue("notifications_enabled", String.valueOf(enabled));
    }

    public String getAppName() {
        return getConfigValue("app_name", "GlobalPay Platform");
    }

    @Transactional
    public void setAppName(String name) {
        setConfigValue("app_name", name);
    }

    public void sendNotification(AppUser user, String message) {
        if (isNotificationsEnabled()) {
            log.info("[SMS/EMAIL NOTIFICATION] Sent to user {}: {} (Email: {}, Phone: {})",
                    user.getUsername(), message, user.getEmail(), user.getPhone());
        }
    }
}
