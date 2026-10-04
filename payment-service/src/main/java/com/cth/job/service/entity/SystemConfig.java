package com.cth.job.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "system_config")
public class SystemConfig {

    @Id
    @Column(name = "config_key", length = 50)
    private String key;

    @Column(name = "config_value", nullable = false, length = 255)
    private String value;

    public SystemConfig() {}

    public SystemConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
