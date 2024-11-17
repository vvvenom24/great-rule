package venom.greatrule;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

class SRADRuleTest {

    @Test
    void adRules() {
        File file = new File("D:\\temp\\sr_Ad.txt");
        Set<String> ruleSet = new HashSet<>();
        try (LineIterator iterator = FileUtils.lineIterator(file)) {
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                if (StringUtils.isNotBlank(line) && line.contains("REJECT")) {
                    String[] split = line.split(",");
                    if (split.length > 2) {
                        ruleSet.add(split[0] + "," + split[1]);
                    } else {
                        System.err.println(line);
                    }
                }
            }
            if (!ruleSet.isEmpty()) {
                File outFile = new File("D:\\temp\\AD.txt");
                FileUtils.writeLines(outFile, "UTF-8", ruleSet, "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
