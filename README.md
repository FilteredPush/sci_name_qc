# sci_name_qc
Data Quality library for dwc:scientificName and dwc:scientificNameAuthorship and related terms

Abstracted from the FilteredPush FP-KurationServices Scientific Name Service classes.

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


## DwCSciNameQC - for Fit4U Framework


# Include using maven

# Building

    mvn package

# Developer deployment: 

To deploy a snapshot to the snapshotRepository: 

    mvn clean deploy

To deploy a new release to maven central, set the version in pom.xml to a non-snapshot version, then deploy with the release profile (which adds package signing and deployment to release staging:

    mvn clean deploy -P release

After this, you will need to login to the sonatype oss repository hosting nexus instance, find the staged release in the staging repositories, and perform the release.  It should be possible (haven't verified this yet) to perform the release from the command line instead by running: 

    mvn nexus-staging:release -P release

