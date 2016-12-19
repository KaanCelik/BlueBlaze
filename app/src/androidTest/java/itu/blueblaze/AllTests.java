package itu.blueblaze;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by KaaN on 19-12-2016.
 * Runs all Instrumented Tests
 */


@RunWith(Suite.class)
@Suite.SuiteClasses({BluetoothTest.class, ExampleInstrumentedTest.class})
public class AllTests {}
