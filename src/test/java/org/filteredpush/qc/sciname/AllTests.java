package org.filteredpush.qc.sciname;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestDwCSciNameDQ.class, 
	TestSciNameUtils.class,
	TaxonTest.class
})
public class AllTests {

}
