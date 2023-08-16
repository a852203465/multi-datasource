package cn.darkrong.multiredis;

import cn.darkrong.multiredis.config.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class RedisUtilsTest extends MultiRedisApplicationTests {

    @Autowired
    @Qualifier("redisCliSec")
    private RedisUtils redisCliSec;

    @Autowired
    @Qualifier("redisCliPri")
    private RedisUtils redisCliPri;

    @Test
    void set() {
        redisCliPri.set("a", 1);
        redisCliSec.set("a", 2);
    }






}
