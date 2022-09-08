package org.filteredpush.qc.sciname.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GBIFServiceTestIT.class, WoRMSServiceTestIT.class, GNIServiceTest_IT.class, IRMNGServiceIT.class })
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
