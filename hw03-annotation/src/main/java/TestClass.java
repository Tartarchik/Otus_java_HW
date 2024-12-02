import annotation.After;
import annotation.Before;
import annotation.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClass {
    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    @Before
    void setUpTest() {
        logger.info("Before");
        throw new RuntimeException("fail test");
    }

    @Test
    void test1() {
        throw new RuntimeException("fail test");
    }

    @Test
    void test3() {
    }

    @Test
    void test2() {
    }

    @After
    void tearDown() {
        logger.info("After");
    }
}
