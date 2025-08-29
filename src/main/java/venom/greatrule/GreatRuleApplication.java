package venom.greatrule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("venom.greatrule.mapper")
public class GreatRuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreatRuleApplication.class, args);
    }

}
