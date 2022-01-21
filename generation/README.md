name_tests.csv are the subset of TDWG BDQ TG2 tests related to NAME (scientific name) terms, excluding the broad measure tests.

DwCSciNameDQ class and Turtle RDF generated using kurator-ffdq using (from a kurator-ffdq directory in the same parent directory as sci_name_qc) with:

   ./test-util.sh -config ../sci_name_qc/generation/sci_name_qc_DwCSciNameQC_kurator_ffdq.config -in ../sci_name_qc/generation/name_tests.csv -out ../sci_name_qc/generation/name_tests.ttl -generateClass -srcDir ../sci_name_qc/src/main/java/
