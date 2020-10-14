package SocialMagnet.Utility;

public class Nth {
    public static String ordinalAbbrev(int n){
        String ans = "th"; //most of the time it should be "th"
        if(n % 100 / 10 == 1) return ans; //teens are all "th"
            switch(n % 10){
            case 1: ans = "st"; break;
            case 2: ans = "nd"; break;
            case 3: ans = "rd"; break;
        }
        return ans;
    }
}