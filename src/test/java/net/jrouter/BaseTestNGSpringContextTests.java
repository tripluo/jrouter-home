package net.jrouter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * BaseTestNGSpringContextTests.
 */
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@Slf4j
public class BaseTestNGSpringContextTests extends AbstractTestNGSpringContextTests {

    @Test
    void showAll() {
//        for (String bn : applicationContext.getBeanDefinitionNames()) {
//            System.out.println(bn + "--->" + applicationContext.getBean(bn));
//        }
    }

    @SpringBootApplication(scanBasePackages = "net.jrouter", exclude = {
            FreeMarkerAutoConfiguration.class,
            ThymeleafAutoConfiguration.class
    })
    static class Config {

    }
}
