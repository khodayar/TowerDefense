package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class is composed of number of test cases which can be performed in a single batch.
 * 
 * @author Team 6
 *
 */
@RunWith(Suite.class)
@SuiteClasses({GameLogTestCase.class, GameGridTestCase.class, GridLocationTestCase.class, GameTestCase.class,
                StrategyTestCase.class, CritterTestCase.class, PathTestCase.class, GameScoreTestCase.class})
public class TestSuite {
}
