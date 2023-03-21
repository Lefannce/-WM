import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest

public class test {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test_01(){
        redisTemplate.opsForValue().set("asc",123);
        System.out.println(redisTemplate.opsForValue().get("asc"));


    }

}
