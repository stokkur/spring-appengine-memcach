package net.stokkur.cloud.memcache;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.HashMap;

public class AppengineCacheManager implements CacheManager {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    private final MemcacheService memcacheService;
    private final HashMap<String, AppengineCache> caches = new HashMap<>();
    private final Expiration defaultExpiration;

    public AppengineCacheManager(MemcacheService memcacheService, Expiration defaultExpiration) {
        log.debug("initialize");
        this.memcacheService = memcacheService;
        this.defaultExpiration = defaultExpiration;
    }

    public MemcacheService getMemcacheService() {
        return memcacheService;
    }

    @Override
    public Cache getCache(String name) {
        log.debug("get cache: " + name);
        Cache cache = caches.get(name);
        if (cache == null) return createCache(name, defaultExpiration);
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        log.debug("get cache names: " + caches.keySet());
        return caches.keySet();
    }

    public void registerCache(String name, Expiration expiration) {
        log.debug("register cache: " + name);
        createCache(name, expiration);
    }

    private AppengineCache createCache(String name, Expiration expiration) {
        log.debug("create cache: " + name);
        AppengineCache cache = new AppengineCache(memcacheService, name, expiration);
        caches.put(name, cache);
        return cache;
    }
}
