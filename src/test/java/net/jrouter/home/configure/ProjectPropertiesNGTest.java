package net.jrouter.home.configure;

import lombok.extern.slf4j.Slf4j;
import net.jrouter.BaseTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ProjectPropertiesNGTest.
 */
@SpringBootTest
@Slf4j
public class ProjectPropertiesNGTest extends BaseTestNGSpringContextTests {

    @Autowired
    private ProjectProperties projectProperties;

    @BeforeMethod
    public void setUpMethod() throws Exception {
        assertNotNull(projectProperties);
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getForwardUrls method, of class ProjectProperties.
     */
    @Test
    public void testGetForwardUrls() throws Exception {
        String[] forwardUrls = projectProperties.getForwardUrls();
        assertNotNull(forwardUrls);
        log.info("Load \"forward-urls\" value : {}", forwardUrls);
    }
}
