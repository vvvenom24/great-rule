package venom.greatrule;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提取clash yaml 配置
 */
class YamlReadTest {

    // 包含常见emoji的Unicode范围
    private final String emojiRegex = "[\\x{1F600}-\\x{1F64F}\\x{1F300}-\\x{1F5FF}\\x{1F680}-\\x{1F6FF}\\x{1F700}-\\x{1F77F}\\x{1F780}-\\x{1F7FF}\\x{1F800}-\\x{1F8FF}\\x{1F900}-\\x{1F9FF}\\x{1FA00}-\\x{1FA6F}\\x{1FA70}-\\x{1FAFF}\\x{2600}-\\x{26FF}\\x{2700}-\\x{27BF}\\x{2300}-\\x{23FF}\\x{2B50}]";
    // 创建正则表达式匹配模式
    private final Pattern pattern = Pattern.compile(emojiRegex);


    @Test
    void testYamlRead() throws IOException {
        File file = new File("D:\\temp\\aaa.yaml");
        boolean start = false;
        Map<String, List<String>> outFiles = new HashMap<>();
        List<String> clean = new ArrayList<>();
        String appName;
        try (LineIterator iterator = FileUtils.lineIterator(file)) {
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                // 从 rule 开始写入到一个新的文件
                if (line.contains("rules")) {
                    start = true;
                }
                if (start && !line.contains("PROCESS-NAME")) {
                    String trim = line.replace("- '", "").replace("'", "").trim();
                    String[] split = trim.split(",");
                    StringBuilder sb = new StringBuilder();
                    appName = null;
                    for (String s : split) {
                        Matcher matcher = pattern.matcher(s);
                        if (!matcher.find()) {
                            sb.append(s).append(",");
                        } else {
                            String group = matcher.group();
                            appName = s.replace(group, "").trim();
                        }
                    }
                    if (Objects.isNull(appName)) {
                        appName = "unknown";
                    }
                    outFiles.compute(appName, (k, v) -> {
                        if (v == null) {
                            v = new ArrayList<>();
                        }
                        v.add(sb.substring(0, sb.length() - 1));
                        return v;
                    });
                }
            }
        }
        outFiles.forEach((k, v) -> {
            File outFile = new File("D:\\temp\\loon\\" + k + ".txt");
            try {
                FileUtils.writeLines(outFile, "UTF-8", v, "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
