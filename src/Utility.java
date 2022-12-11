import java.util.ArrayList;
import java.util.List;

class Utility {
    public static List<String> split(String text, char splitChar){
        List<String> splitText = new ArrayList<>();
        int currentIndex = 0;
        int startIndex = 0;
        for (char character : text.toCharArray()){
            if (character == splitChar){
                splitText.add(substring(text, startIndex, currentIndex));
                startIndex = currentIndex + 1;
            }
            currentIndex++;
        }

        //has split -> add last text
        if(startIndex != 0){
            splitText.add(substring(text, startIndex, text.length()));
        }else{
            splitText.add(text);
        }
        return splitText;
    }

    public static List<String> split(String text, char[] regex){
        List<String> splitText = new ArrayList<>();
        int currentIndex = 0;
        int startIndex = 0;
        boolean hasEntered = false;
        for (char character : text.toCharArray()){
            for(char regexCharacter : regex) {
                if (regexCharacter == character && !hasEntered) {
                    if(startIndex == currentIndex){
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
        if(startIndex != 0 && startIndex != currentIndex){
            splitText.add(substring(text, startIndex, text.length()));
        }
        return splitText;
    }

    public static String substring(String text, int index){
        char[] substringArray = new char[text.length() - index];
        int counter = 0;
        for (int i = index; i < text.length(); i++, counter++){
            substringArray[counter] = charAt(text, i);
        }

        return new String(substringArray);
    }

    public static String substring(String text, int start, int end){
        char[] substringArray = new char[end - start];
        int counter = 0;
        for (int i = start; i < end; i++, counter++){
            substringArray[counter] = charAt(text, i);
        }

        return new String(substringArray);
    }

    public static char charAt(String text, int index) {
        return text.toCharArray()[index];
    }

    public static String join(List<String> list, String delimiter){ //Make StringBuilder...
        String concatText = "";
        for(int i = 0; i < list.size() - 1; i++){
            concatText += list.get(i) + delimiter;
        }
        return concatText;
    }

    public static String toLowerCase(String text) {
        char[] textToLowerCase = new char[text.length()];
        int counter = 0;
        for (char letter : text.toCharArray()){
            //check if upper case...
            if((int)Utility.charAt(text, counter) >= 65 && (int)Utility.charAt(text, counter) <= 90){
                textToLowerCase[counter] = (char)((int)Utility.charAt(text, counter) + 32);
            }else{
                textToLowerCase[counter] = Utility.charAt(text, counter);
            }
            counter++;
        }
        return new String(textToLowerCase);
    }
}
