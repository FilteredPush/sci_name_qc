name_tests.csv are the subset of TDWG BDQ TG2 tests related to NAME (scientific name) terms, excluding the broad measure tests.

    head -n 1 ../../bdq/tg2/core/TG2_tests.csv > name_tests.csv
    grep NAME ../../bdq/tg2/core/TG2_tests.csv  | grep -v AllDarwinCoreTerms | grep -v ISSUE_DATAGENERALIZATIONS_NOTEMPTY  >> name_tests.csv
    grep NAME ../../bdq/tg2/supplementary/TG2_supplementary_tests.csv  | grep -v AllDarwinCoreTerms | grep -v ISSUE_DATAGENERALIZATIONS_NOTEMPTY  >> name_tests.csv

Stub DwCSciNameDQ class and Turtle RDF generated using kurator-ffdq using (from a kurator-ffdq directory in the same parent directory as sci_name_qc) with:

   ./test-util.sh -config ../sci_name_qc/generation/sci_name_qc_DwCSciNameQC_stubs_kurator_ffdq.config -in ../sci_name_qc/generation/name_tests.csv -out ../sci_name_qc/generation/name_tests.ttl -generateClass -srcDir ../sci_name_qc/src/main/java/

Add comments to the end of java class noting out of date implementations using kurator-ffdq (from a kurator-ffdq directory in the same parent directory as sci_name_qc) with: 

   ./test-util.sh -config ../sci_name_qc/generation/sci_name_qc_DwCSciNameQC_kurator_ffdq.config -in ../sci_name_qc/generation/name_tests.csv -out ../sci_name_qc/generation/name_tests.ttl -checkVersion -appendClass -srcDir ../sci_name_qc/src/main/java/
