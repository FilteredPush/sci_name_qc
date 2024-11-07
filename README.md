# sci_name_qc
Data Quality library for dwc:scientificName and dwc:scientificNameAuthorship and related terms

Abstracted from the FilteredPush FP-KurationServices Scientific Name Service classes.

DOI: 10.5281/zenodo.7026712

[![DOI](https://zenodo.org/badge/70093157.svg)](https://zenodo.org/badge/latestdoi/70093157)

## Primitives

### AuthorNameComparator

Provides AuthorNameComparator, a small set of classes to make nomenclatural code and convention aware comparisons of scientific name authorship strings. Example use: 

     String authorship = "(J. C. Schmidt) Coker & Beers ex Pouzar: Fries";
     AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(authorship, null);
     // Botanical formulation is recognised, and comparator is an ICNafpAuthorNameComparator.
     String result = comparator.compare("(J. C. Schmidt) Coker & Beers ex Pouzar: Fries","(Schmidt) Coker & Beers ex Pouzar: Fr.").getMatchType();
     System.out.println(result);  // Same Author, but abbreviated differently J. C. Schmidt equated with Schmidt, Fries equated with Fr..
     result = comparator.compare("(J. C. Schmidt) Coker & Beers ex Pouzar: Fries","(Schmidt) Coker & Beers ex Pouzar; Fries").getMatchType();
     System.out.println(result);  // Authorship botanical parts tokenize differently, recognizes : to mark sanctioning author Fries isn't present.

Returns:

     Same Author, but abbreviated differently.
     Authorship botanical parts tokenize differently

Currently recognised comparisons: 

     public static final String MATCH_EXACT = "Exact Match";
     public static final String MATCH_ERROR = "Error in making comparison";
     public static final String MATCH_CONNECTFAILURE = "Error connecting to service";
     public static final String MATCH_FUZZY_SCINAME = "Fuzzy Match on Scientific Name";
     public static final String MATCH_DISSIMILAR = "Author Dissimilar";
     public static final String MATCH_STRONGDISSIMILAR = "Author Strongly Dissimilar";
     /**
      * Zoological, "Sowerby" in one case, Sowerby I, Sowerby II, or Sowerby III in the other,
      * specification of which of the Sowerby family was the author was added.
      */
     public static final String MATCH_SOWERBYEXACTYEAR = "Specifying Which Sowerby, Year Exact";
     /**
      * Zoological, L. may be abbreviation for Linnaeus or Lamarck.
      * Does not apply to Botany.
      */
     public static final String MATCH_L_EXACTYEAR = "Ambiguous L., Year Exact";
     /**
      * Zoological, L. may be abbreviation for Linnaeus or Lamarck.
      * Does not apply to Botany.
      */
     public static final String MATCH_L = "Ambiguous L.";
     public static final String MATCH_WEAKEXACTYEAR = "Slightly Similar Author, Year Exact";
     public static final String MATCH_SIMILAREXACTYEAR = "Similar Author, Year Exact";
     public static final String MATCH_SIMILARMISSINGYEAR = "Similar Author, Year Removed";
     public static final String MATCH_SIMILARADDSYEAR = "Similar Author, Year Added";
     public static final String MATCH_EXACTDIFFERENTYEAR = "Exact Author, Years Different";
     public static final String MATCH_EXACTMISSINGYEAR = "Exact Author, Year Removed";
     public static final String MATCH_EXACTADDSYEAR = "Exact Author, Year Added";
     public static final String MATCH_PARENTHESIESDIFFER = "Differ only in Parenthesies";
     public static final String MATCH_PARENYEARDIFFER = "Differ in Parenthesies and Year";
     public static final String MATCH_AUTHSIMILAR = "Author Similar";
     public static final String MATCH_ADDSAUTHOR = "Author Added";
     public static final String MATCH_MULTIPLE = "Multiple Matches:";
     /**
      * Botanical parenthetical, ex, sanctioning author bits are composed differently, without
      * regard to the similarity or difference of the individual bits.  e.g. "(x) y" is 
      * different from "x ex y" or (x) y ex z", but not different in this comparison from "(a) b".
      */
     public static final String MATCH_PARTSDIFFER = "Authorship botanical parts tokenize differently";
     /**
      * Authorship strings appear similar (parenthesies, botanical functional parts), but the differences
      * between the strings can be explained by differences in abbreviation or addition/removal of initials.
      */
     public static final String MATCH_SAMEBUTABBREVIATED = "Same Author, but abbreviated differently.";

### SciNameUtils

Limited command line access is provided to process a CSV file containing
taxon names, checking those names against a specified authority and asserting
matches of those names against the authority.

#### Command line usage

    $ java -jar sci_name_qc-1.1.3-SNAPSHOT-{commit}-executable.jar --help
    usage: SciNameUtils
     -f,--file <arg>      Input csv file from which to lookup names.  Assumes
                          a csv file, first three columns being dbpk,
                          scientificname, authorship, (TODO: family), columns
                          after the third are ignored.
     -h,--help            Print this message
     -o,--output <arg>    Output file into which to write results of lookup,
                          default output.csv
     -s,--service <arg>   Service to lookup names against  WoRMS,
                          GBIF_BACKBONE, GBIF_ITIS, GBIF_FAUNA_EUROPEA,
                          GBIF_UKSI, GBIF_IPNI, GBIF_INDEXFUNGORUM, GBIF_COL,
                          GBIF_PALEOBIOLOGYDB, or ZooBank (TODO:
                          WoRMS+ZooBank).
     -t,--test            Test connectivity with an example name

#### Processing output to sql

Example regex (in vim) to convert csv output to sql queries to add WoRMS guids to MCZbase:

	%s/"\([0-9]\+\)","[A-Za-z.() ]\+",".*","\(urn:lsid:marinespecies.org:taxname:[0-9]\+\)",".*$/update taxonomy set taxonid = '\2', taxonid_guid_type = 'WoRMS LSID' where taxonid is null and taxon_name_id = \1;/


## DwCSciNameQC - for Fit4U Framework

Class: org.filteredpush.qc.sciname.DwCSciNameDQ

Implements the TDWG BDQ TG2 Scientific Name (NAME) tests.


# Include using maven

Available in Maven Central.  

    <dependency>
        <groupId>org.filteredpush</groupId>
        <artifactId>sci_name_qc</artifactId>
        <version>1.1.2</version>
    </dependency>


# Building

To build an executable jar in the project directory (sci_name_qc-{version}-{commit}-executable.jar), run:

    mvn package

To install in your local maven repository run:

	mvn install

If javadoc errors in generated code are blocking a build and you want to be able to produce an artifact before addressing those, you can 
temporarily (these will still need to be addressed before deployment) suppress them to be just warnings with:

   mvn clean package -DadditionalJOption=-Xdoclint:none

# Testing on and offline

Test are separated into those be run offline and those that require conection to remote services (GBIF API, WoRMS aphia API).

Tests requiring online access to remote services are run in the integration-test phase, which lies between the package and install phases.  So, invocation of

    mvn package

will run the tests that do not need online access to services, (as will mvn test), while invocation of

	mvn install

will run both these test and the integration tests that require access to online services (similarly mvn deploy).  
It is currently safe to invoke mvn integration-test from the command line, but this is not a recommended practice, 
as integration-tests are typically expected to start/stop a local jetty server in pre- and post- phases.  If you
have to run mvn install in an offline environment, you can use mvn install -DskipTests to prevent test failures
from the absence of a connection to GBIF and WoRMS from causing the build to fail.

# Developer deployment: 

To deploy a snapshot to the snapshotRepository: 

    mvn clean deploy

To deploy a new release to maven central, set the version in pom.xml and in metadata to a non-snapshot version, then deploy with the release profile (which adds package signing and deployment to release staging:

1. set version in pom.xml

2. set version in Mechanism annotation in the DwCSciNameDQ classes in the generation configuration files, and in this README.

	src/main/java/org/filteredpush/qc/sciname/DwCSciNameDQ.java
	src/main/java/org/filteredpush/qc/sciname/DwCSciNameDQDefaults.java
	generation/sci_name_qc_DwCSciNameQC_kurator_ffdq.config
	generation/sci_name_qc_DwCSciNameQC_stubs_kurator_ffdq.config
	README.md

3. deploy to maven central

    mvn clean deploy -P release

After this, you may login to the sonatype oss repository hosting nexus instance find the staged release in the staging repositories and confirm the release.  

