package com.reg.ssoreg;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SsoRegApplicationTests {



    @Test
    void contextLoads() {
        double i1  = 42.088988146366546;
        double o1  = 39.20000000000002;
        double i2  = 49.068029548187155;
        double o2  = 45.69999999999987;

        System.out.println((i1 - o1) / i1);
        System.out.println((i2 - o2) / i2);
    }

}
