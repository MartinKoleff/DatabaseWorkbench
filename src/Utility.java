import java.util.ArrayList;
import java.util.Date;
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

    public static List<String> split(String text, String word) {
        List<String> splitText = new ArrayList<>();
        int startIndex = 0;

        for (int i = 0; i < text.length() - word.length(); i++) {
            if (Utility.substring(text, i, word.length() + i).equals(word)) {
                splitText.add(Utility.substring(text, startIndex, i));
                i += word.length();
                startIndex = i;
            }
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
        for (int i = 0; i <= list.size() - 1; i++) {
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

    public static String concat(List<String> list, char concatChar) { //Make StringBuilder...
        String concatText = "";
        for (String words : list) {
            concatText += words + concatChar;
        }
        return concatText;
    }

    public static List<String> trimList(List<String> list) {
        List<String> returnList = new ArrayList<>();
        for (String element : list) {
            if (!element.isEmpty() && !element.isBlank()) {
                returnList.add(element);
            }
        }

        return returnList;
    }


    static class Parser<T extends Object> {

        //Get column types from dataBase .txt file 2nd row...
        public static boolean tryParse(List<String> userInput, List<String> userInputColumnTypes) {
            for (int i = 0; i < userInputColumnTypes.size(); i++) {
                try {
                    String currentColumnType = userInputColumnTypes.get(i);
                    switch (currentColumnType) {
                        case "int":
                            if(!validateInt(userInput.get(i))){
                                return false;
                            }
                            continue;
                        case "string":
//                            return true;
//                            return validateString(userInput.get(i)); //ASCII Table range of lower/upper case try each char...
                            continue;
                        case "date":
                            if(!validateDate(userInput.get(i))){
                                return false;
                            }
                            continue;
                        default:
                            return false; //Wrong input...
                    }
                } catch (IndexOutOfBoundsException e) { //The columns for insert might be less than total columns...
                    return true;
                }
            }
            return true;
        }

        public static boolean tryParse(String userInput, String userInputColumnType) {
            try {
                switch (Utility.toLowerCase(userInputColumnType)) {
                    case "int":
                        return validateInt(userInput);
                    case "string":
                        return true;
                    case "date":
                        return validateDate(userInput);
                    default:
                        return false; //Wrong input...
                }
            } catch (IndexOutOfBoundsException e) {
                return true;
            }
        }

        private static boolean validateDate(String text) {
            int asciiValue; //Pattern -> dd.mm.yyyy
            for (int i = 0; i < text.length(); i++) {
                if ((i == 2 || i == 5) && Utility.charAt(text, i) == '.') continue;

                asciiValue = (int) Utility.charAt(text, i);
                if (!(asciiValue <= 57 && asciiValue >= 48)) {
                    return false;
                }
            }

            return text.length() == 10;
        }

        private static boolean validateInt(String text) {
            int asciiValue; //ASCII Table range of numbers try each char...
            for (int i = 0; i < text.length(); i++) {
                asciiValue = (int) Utility.charAt(text, i);
                if (!(asciiValue <= 57 && asciiValue >= 48)) {
                    return false;
                }
            }
            return true;
        }

        public static boolean compareDates(Date date1, Date date2, String mathSign) throws Exception{
            int result = date1.compareTo(date2);
            switch (mathSign) {
                case "<=":
                    return result <= 0;
                case "<":
                    return result < 0;
                case ">=":
                    return result >= 0;
                case ">":
                    return result > 0;
                case "=":
                    return result == 0;
                case "<>":
                    return result != 0;
                default:
                    throw new Exception("Invalid math sign.");
            }
        }

        public static boolean compareInts(int number1, int number2, String mathSign) throws Exception {
            switch (mathSign) {
                case "<=":
                    return number1 <= number2;
                case "<":
                    return number1 < number2;
                case ">=":
                    return number1 >= number2;
                case ">":
                    return number1 > number2;
                case "=":
                    return number1 == number2;
                case "<>":
                    return number1 != number2;
                default:
                    throw new Exception("Invalid math sign.");
            }
        }
    }
}
