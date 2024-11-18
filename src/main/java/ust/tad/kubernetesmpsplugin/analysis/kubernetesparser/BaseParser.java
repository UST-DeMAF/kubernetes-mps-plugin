package ust.tad.kubernetesmpsplugin.analysis.kubernetesparser;

public class BaseParser {
    public static int countLeadingWhitespaces(String string) {
        int spaceCount = 0;
        for (char c : string.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
            else {
                return spaceCount;
            }
        }
        return spaceCount;
    }
}
