package net.jrouter.framework.configure;

import lombok.extern.slf4j.Slf4j;
import net.jrouter.ActionFactory;
import net.jrouter.BaseTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ActionFactoryConfigurationNGTest.
 */
@SpringBootTest
@Slf4j
public class ActionFactoryConfigurationNGTest extends BaseTestNGSpringContextTests {

    @Autowired
    private ActionFactory<String> actionFactory;

    @BeforeMethod
    public void setUpMethod() throws Exception {
        assertNotNull(actionFactory);
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of actionFactoryFilterRegistration method, of class ActionFactoryConfiguration.
     */
    @Test
    public void testActionFactoryFilterRegistration() {
    }

}
