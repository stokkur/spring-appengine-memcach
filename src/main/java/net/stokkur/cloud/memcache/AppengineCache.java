package net.stokkur.cloud.memcache;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleKey;

import java.util.concurrent.Callable;

public class AppengineCache implements Cache {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    private final MemcacheService memcacheService;
    private final String name;
    private final Expiration expiration;

    public AppengineCache(MemcacheService memcacheService, String name, Expiration expiration) {
        this.memcacheService = memcacheService;
        this.name = name;
        this.expiration = expiration;
    }

    @Override
    public String getName() {
        log.debug("name: " + name);
        return name;
    }

    @Override
    public Object getNativeCache() {
        return memcacheService;
    }

    @Override
    public ValueWrapper get(Object key) {
        log.debug("get wrapper: " + key);
        key = getKey(key);
        Object value = memcacheService.get(key);
        if (value != null || memcacheService.contains(key)) return (() -> value);
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        log.debug("get: " + key);
        return (T) memcacheService.get(getKey(key));
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("get loader: " + key);
        Object g = memcacheService.get(getKey(key));
        if (g != null) return (T) g;
        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

    @Override
    public void put(Object key, Object value) {
        log.debug("put: " + getKey(key) + ", value: " + value);
        memcacheService.put(getKey(key), value, expiration);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("put if absent: " + key + ", value: " + value);
        if (memcacheService.put(getKey(key), value, expiration, MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT)) return null;
        return () -> memcacheService.get(getKey(key));
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict key: " + key);
        memcacheService.delete(getKey(key));
    }

    /**
     * This will clear the entire cache, not just the cache for this name.
     */
    @Override
    public void clear() {
        memcacheService.clearAll();
    }

    private Object getKey(Object key) {
        if (key instanceof String) return name +"-" + key;
        return new SimpleKey(name, key);
    }
}
