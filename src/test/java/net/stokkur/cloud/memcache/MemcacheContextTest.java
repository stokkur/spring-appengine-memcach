package net.stokkur.cloud.memcache;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"appengine.memcache.enabled=true", "appengine.memcache.expiration=10"},
        classes = {AppengineConfiguration.class, FooService.class, Conf.class})
@Ignore
public class MemcacheContextTest {

    @Autowired
    private AppengineCacheManager appengineCacheManager;

    @Autowired FooService fooService;

    @Test
    public void runs() {
        Assert.assertNotNull(appengineCacheManager);
    }

    @Test
    public void cachesFoo() {
        fooService.foobar("s", "s");
        fooService.foobar("s", "s");
    }
}

@Configuration
@EnableCaching
class Conf {}

@Service
class FooService {

    @Cacheable("foos")
    public String foobar(String s, String d) {
        System.out.println("smu");
        return "FooBar";
    }
}
