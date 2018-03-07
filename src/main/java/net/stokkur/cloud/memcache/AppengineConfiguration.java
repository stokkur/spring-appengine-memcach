package net.stokkur.cloud.memcache;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(AppengineMemcacheProperties.class)
@ConditionalOnProperty(prefix = "appengine.memcache", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppengineConfiguration {

    @Bean
    public CacheManager cacheManager(AppengineMemcacheProperties properties, Optional<CacheConfiguration> cacheConfiguration) {
        MemcacheService memcacheService;
        if (properties.getNamespace() != null) {
            memcacheService = MemcacheServiceFactory.getMemcacheService(properties.getNamespace());
        } else {
            memcacheService = MemcacheServiceFactory.getMemcacheService();
        }
        Expiration expiration = null;
        if (properties.getExpiration() != null) expiration = Expiration.byDeltaSeconds((int) properties.getExpiration().getSeconds());
        AppengineCacheManager manager = new AppengineCacheManager(memcacheService, expiration);
        cacheConfiguration.ifPresent(configuration -> configuration.forEach(manager::registerCache));
        return manager;
    }
}
