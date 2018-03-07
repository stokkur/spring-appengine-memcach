package net.stokkur.cloud.memcache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("appengine.memcache")
public class AppengineMemcacheProperties {
    private Boolean enabled = true;
    private String namespace = null;
    private Duration expiration = null;

    public Boolean getEnabled() {
        return enabled;
    }

    public AppengineMemcacheProperties setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public AppengineMemcacheProperties setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public Duration getExpiration() {
        return expiration;
    }

    public AppengineMemcacheProperties setExpiration(Duration expiration) {
        this.expiration = expiration;
        return this;
    }
}
