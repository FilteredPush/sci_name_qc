package org.filteredpush.qc.sciname;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	DwCSciNameDQ_IT.class 
})
/** 
 * Tests which require online access to remote services, named, to fit the expectations of the
 * maven-failsafe-plugin with an IT (integration test) ending, so as to run in the integration-test
 * phase.  See the maven-failsafe-plugin section in pom.xml
 * 
 * @author mole
 *
 */
public class AllTestsIT {

}
