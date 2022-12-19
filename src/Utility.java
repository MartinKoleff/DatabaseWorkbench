import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static Parser parser;

    public static List<String> split(String text, char splitChar) {
        List<String> splitText = new ArrayList<>();
        int currentIndex = 0;
        int startIndex = 0;
        for (char character : text.toCharArray()) {
            if (character == splitChar) {
                splitText.add(substring(text, startIndex, currentIndex));
                startIndex = currentIndex + 1;
            }
            currentIndex++;
        }

        //has split -> add last text
        if (startIndex != 0) {
            if (startIndex == text.length()) return splitText;
            splitText.add(substring(text, startIndex, text.length()));
        } else {
            splitText.add(text);
        }
        return splitText;
    }

    public static List<String> split(String text, char[] regex) {
        List<String> splitText = new ArrayList<>();
        int currentIndex = 0;
        int startIndex = 0;
        boolean hasEntered = false;
        for (char character : text.toCharArray()) {
            for (char regexCharacter : regex) {
                if (regexCharacter == character && !hasEntered) {
                    if (startIndex == currentIndex) {
                        startIndex++;
                        break;
                    }
                    splitText.add(substring(text, startIndex, currentIndex));
                    startIndex = currentIndex + 1;
                    hasEntered = true;
                }
            }
            hasEntered = false;
            currentIndex++;
        }

        //has split -> add last text
        if (startIndex != 0) {
            if (startIndex == text.length()) return splitText;
            splitText.add(substring(text, startIndex, text.length()));
        } else {
            splitText.add(text);
        }
        return splitText;
    }

    public static String substring(String text, int index) {
        char[] substringArray = new char[text.length() - index];
        int counter = 0;
        for (int i = index; i < text.length(); i++, counter++) {
            substringArray[counter] = charAt(text, i);
        }

        return new String(substringArray);
    }

    public static String substring(String text, int start, int end) {
        char[] substringArray = new char[end - start];
        int counter = 0;
        for (int i = start; i < end; i++, counter++) {
            substringArray[counter] = charAt(text, i);
        }

        return new String(substringArray);
    }

    public static char charAt(String text, int index) {
        return text.toCharArray()[index];
    }

    public static String join(List<String> list, String delimiter) { //Make StringBuilder...
        String concatText = "";
        for (int i = 0; i < list.size() - 1; i++) {
            concatText += list.get(i) + delimiter;
        }
        return concatText;
    }

    public static String toLowerCase(String text) {
        char[] textToLowerCase = new char[text.length()];
        int counter = 0;
        for (char letter : text.toCharArray()) {
            //check if upper case...
            if ((int) Utility.charAt(text, counter) >= 65 && (int) Utility.charAt(text, counter) <= 90) {
                textToLowerCase[counter] = (char) ((int) Utility.charAt(text, counter) + 32);
            } else {
                textToLowerCase[counter] = Utility.charAt(text, counter);
            }
            counter++;
        }
        return new String(textToLowerCase);
    }

    public static String concat(List<String> list, char concatChar) {
        String concatText = "";
        for (String words : list) {
            concatText += words + concatChar;
        }
        return concatText;
    }

    public static List<String> trimList(List<String> list) {
        List<String> returnList = new ArrayList();
        for (String element : list) {
            if (!element.isEmpty() && !element.isBlank()) {
                returnList.add(element);
            }
        }

        return returnList;
    }

    static class Parser<T extends Object> {
        //Get column types from dataBase .txt file 2nd row...
        public static boolean tryParse(List<String> correctColumnType, List<String> userInput) {
            for (int i = 0; i < correctColumnType.size(); i++) {
               try {
                   switch (correctColumnType.get(i)) {
                       case "int":
                           //ASCII Table range of numbers try each char...
                           break;
                       case "string":
                           //ASCII Table range of lower/upper case try each char...
                           break;
                       case "date":
                           //Pattern -> dd.mm.yyyy
                           break;
                       default:
                           //Wrong input...
                           return false;
                   }
               }catch (IndexOutOfBoundsException e){
                   return true;
               }
            }
            return true;
        }
    }
}
