package org.analyzer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class FastaAnalyzerTest {

    @Test
    void alphabetCountTest() throws FileNotFoundException {
        Map<Character, Integer> testMap = new HashMap<>();
        testMap.put('A' , 1);
        testMap.put('T' , 2);
        testMap.put('G' , 1);
        testMap.put('C' , 2);
        FastaHandler testhandler = FastaHandler.getInstance();
        File fastatest = new File("/home/moron/fastaAnalyzer/src/main/java/org/analyzer/test.fasta");
        testhandler.parseFasta(fastatest, "DNA");
        FastaEntry testObject = testhandler.entryList.get(0);

        Map<Character, Double> foobar = testObject.getAlphabetCount();

        assertEquals(testMap, foobar);





    }
}
