import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogBack {
    private final static Logger logger = LoggerFactory.getLogger(SpringbootStudyLoggerApplicationTests.class);

    @Test
    public void log() {
        logger.info("==============================================");
        logger.debug("==============================================");
        logger.error("==============================================");
    }

}
