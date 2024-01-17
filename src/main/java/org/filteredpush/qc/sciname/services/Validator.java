/**
 * 
 */
package org.filteredpush.qc.sciname.services;

import java.util.List;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * Interface for data sources that are capable of validating scientific
 * name usages.  A consumer of NameUsages returned by validate() is responsible
 * for serialization of those objects to output.
 *
 * @author mole
 * @version $Id: $Id
 */
public interface Validator {

	/**
	 * <p>validate.</p>
	 *
	 * @param taxonToValidate a {@link edu.harvard.mcz.nametools.NameUsage} object.
	 * @return a {@link edu.harvard.mcz.nametools.NameUsage} object.
	 * @throws org.filteredpush.qc.sciname.services.ServiceException if any.
	 */
	public NameUsage validate(NameUsage taxonToValidate) throws ServiceException;
	
	/**
	 * <p>supportedExtensionTerms.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> supportedExtensionTerms();
	
}
