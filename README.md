# AppEngine Memcache Spring support

Support for memcache in appengine in Spring


This project will use spring auto configuration, for easy of use.
Just include this dependency and set the properties. Remember all items nedds to be serializable.

You need to set config in your application.properties file.
```properties
appengine.memcache.enabled=true # false will disable this caching bean. Normal spring cache will take over, you may need to disable that one as well if you want to disable all caching.
appengine.memcache.expiration=100s # default expiration for entities, null means no expiration
appengine.memcache.namespace=mynamespace # namespace to use, optional
```

To configure a cache names individually. 
```java
@Configuration
@EnableCaching
public class AppConfiguration {

    @Bean
    public CacheConfiguration cacheConfiguration() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.put("foo", Expiration.byDeltaSeconds(20));
        return cacheConfiguration;
    }
}
```
