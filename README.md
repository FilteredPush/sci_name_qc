# sci_name_qc
Data Quality library for dwc:scientificName and dwc:scientificNameAuthorship and related terms

Abstracted from the FilteredPush FP-KurationServices Scientific Name Service classes.

## SciNameUtils - primitives


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

