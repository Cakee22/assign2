import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.example.MemAppender;
import org.example.VelocityLayout;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class StressTest {
    private static Logger logger;
    private static MemAppender memAppender;
    private static VelocityLayout velocityLayout;
    private static PatternLayout patternLayout;
    @BeforeAll
    public static void setup () {
        BasicConfigurator.configure();
        logger = Logger.getLogger("Tester");
        logger.setAdditivity(false);
        velocityLayout = new VelocityLayout("[$p] ($t) $d: $m $n");
        patternLayout = new PatternLayout("[%p] (%t) %d: %m %n");
    }

    @AfterEach
    public void resetData () {
        logger.removeAllAppenders();
    }

    @AfterAll
    public static void removeTestData () throws IOException {
        Files.deleteIfExists(
                Paths.get("testLogs.txt"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    void memAppender_with_ArrayList_patternLayout(int size){
        memAppender=MemAppenderTest.getNewInstance();
        memAppender.setMaxSize(size);
        memAppender.setLayout(patternLayout);
        logger.addAppender(memAppender);
        // Thread.sleep(10000); used for profiling purposes
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }

    }
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    void memAppender_with_ArrayList_velocityLayout(int size){
        memAppender=MemAppenderTest.getNewInstance();
        memAppender.setMaxSize(size);
        memAppender.setLayout(velocityLayout);
        logger.addAppender(memAppender);
        // Thread.sleep(10000); used for profiling purposes
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    void memAppender_with_LinkedList_patternLayout(int size) {
        memAppender = MemAppenderTest.getNewInstance();
        memAppender.setMaxSize(size);
        memAppender.setLayout(patternLayout);
        logger.addAppender((memAppender));
        for(int i=1; i<=10000; i++){
            logger.error("Test number " + i, new Exception());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    void memAppender_with_LinkedList_velocityLayout(int size) {
        memAppender = MemAppenderTest.getNewInstance();
        memAppender.setMaxSize(size);
        memAppender.setLayout(velocityLayout);
        for(int i=1; i<=10000; i++){
            logger.error("Test number " + i, new Exception());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000,100000})
    @Disabled
    void consoleAppender_with_velocityLayout()throws Exception{
        Logger logger = Logger.getLogger(StressTest.class);

        Layout velocityLayout = new VelocityLayout();

        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(velocityLayout);
        consoleAppender.setThreshold(Level.ALL);
        consoleAppender.activateOptions();

        Logger.getRootLogger().addAppender(consoleAppender);

        for(int i=1; i<=10000; i++){
            logger.error("Test number " + i, new Exception());

        }


    }


    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000,100000})
    @Disabled
    void consoleAppender_with_patternLayout()throws Exception{

        Logger logger = Logger.getLogger(StressTest.class);

        Layout patternLayout = new PatternLayout();

        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(patternLayout);
        consoleAppender.setThreshold(Level.ALL);
        consoleAppender.activateOptions();

        Logger.getRootLogger().addAppender(consoleAppender);

        for(int i=1; i<=10000; i++){
            logger.error("Test number " + i, new Exception());

        }
    }




}
