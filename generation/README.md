The name_tests.csv are the subset of TDWG BDQ TG2 tests related to NAME (scientific name) terms, excluding the broad measure tests.

Run the following commands to obtain a current list of NAME related tests in sci_name_qc/generation/name_tests.csv:
    
    cd sci_name_qc/generation/
    head -n 1 ../../bdq/tg2/core/TG2_tests.csv > name_tests.csv
    grep NAME ../../bdq/tg2/core/TG2_tests.csv  | grep -v AllValidationTestsRunOnSingleRecord | grep -v AllAmendmentTestsRunOnSingleRecord | grep -v ISSUE_DATAGENERALIZATIONS_NOTEMPTY  >> name_tests.csv
    grep NAME ../../bdq/tg2/supplementary/TG2_supplementary_tests.csv  | grep -v AllDarwinCoreTerms | grep -v AllValidationTestsRunOnSingleRecord | grep -v AllAmendmentTestsRunOnSingleRecord  | grep -v ISSUE_DATAGENERALIZATIONS_NOTEMPTY  >> name_tests.csv
    cp ../../bdq/tg2/core/TG2_tests_additional_guids.csv additional_guids.csv
    grep -v Method ../../bdq/tg2/supplementary/TG2_supplementary_additional_guids.csv >> additional_guids.csv

From this, RDF can be generated to use to obtain test metadata from the test IRIs, or Java code can be generated or marked as needing updates using kurator-ffdq.

Generate Turtle RDF using kurator-ffdq using (from a kurator-ffdq directory in the same parent directory as sci_name_qc) with:

   ./test-util.sh -config ../sci_name_qc/generation/sci_name_qc_DwCEventDQ_kurator_ffdq.config -in ../sci_name_qc/generation/time_tests.csv -out ../sci_name_qc/generation/time_tests.ttl -ieGuidFile ../bdq/tg2/core/information_element_guids.csv  -guidFile ../sci_name_qc/generation/additional_guids.csv

Stub DwCSciNameDQ class and Turtle RDF can be generated using kurator-ffdq using (from a kurator-ffdq directory in the same parent directory as sci_name_qc) with:

   ./test-util.sh -config ../sci_name_qc/generation/sci_name_qc_DwCSciNameQC_stubs_kurator_ffdq.config -in ../sci_name_qc/generation/name_tests.csv -out ../sci_name_qc/generation/name_tests.ttl -generateClass -srcDir ../sci_name_qc/src/main/java/ -ieGuidFile ../bdq/tg2/core/information_element_guids.csv  -guidFile ../event_date_qc/generation/additional_guids.csv

Add comments to the end of a java class noting out of date implementations and adding stubs for any unimplemented methods using kurator-ffdq (from a kurator-ffdq directory in the same parent directory as sci_name_qc) with: 

   ./test-util.sh -config ../sci_name_qc/generation/sci_name_qc_DwCSciNameQC_kurator_ffdq.config -in ../sci_name_qc/generation/name_tests.csv -out ../sci_name_qc/generation/name_tests.ttl -checkVersion -appendClass -srcDir ../sci_name_qc/src/main/java/ -ieGuidFile ../bdq/tg2/core/information_element_guids.csv  -guidFile ../event_date_qc/generation/additional_guids.csv
