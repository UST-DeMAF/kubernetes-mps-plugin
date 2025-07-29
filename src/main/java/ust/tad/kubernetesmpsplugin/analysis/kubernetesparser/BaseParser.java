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

    /**
     * Utility function to get a string value free of comments and leading and trailing whitespaces.
     *
     * @param value the value to clean.
     * @return the cleaned value.
     */
    public static String getCleanedValue(String value) {
        return value.split("#")[0].trim();
    }
}
