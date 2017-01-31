package edu.harvard.mcz.nametools.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestICZNAuthorNameComparator.class, 
	TestICNafpAuthorNameComparator.class 
	})
public class AllTests {

}
