package SocialMagnet.Menu;
import java.lang.Math;
import java.util.*;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Social.Post.*;
import SocialMagnet.Control.ThreadCtrl;

/**
 * MenuUtility
 * Helper functions for menus
 */

public class MenuUtility {

    // Handles incorrect input types for menus (int only)
    public int checkInput() {
        Scanner sc = new Scanner(System.in);
        int choice;
        try {
            choice = sc.nextInt();
        } catch (InputMismatchException e) {
            choice = 0;
        }
        return choice;
    }

    // Displays header for city farmers
    public void displayCityFarmersHeader(String category, Member user) {
        if (!category.equals("")) {
            category = ":: " + category + " ";
        }

        System.out.println();
        System.out.println("== Social Magnet :: City Farmers " + category + "==");
        System.out.println("Welcome, " + user.getFullName() + "!");
        System.out.println("Title: " + user.getRank() + "      Gold: " + user.getGold() + " gold");
        System.out.println();
    }

    public void displayPosts(Dictionary postDictionary) {
        ThreadCtrl ctrl = new ThreadCtrl();

        for (int i = 1; i <= 5; i++) {
            if (postDictionary.get(i) != null) {
                Post post = (Post)postDictionary.get(i);
                post.printPost(i);
                int postID = post.getPostID();

                // retrieving number of likes and dislikes
                ArrayList<Member> likers = ctrl.retrieveLikers(postID);
                int numOfLikes = likers.size();
                ArrayList<Member> dislikers = ctrl.retrieveDislikers(postID);
                int numOfDislikes = dislikers.size();

                String likesPlural = "";
                if (numOfLikes >= 2) {
                    likesPlural += "s";
                }

                String dislikesPlural = "";
                if (numOfDislikes >= 2) {
                    dislikesPlural += "s";
                }

                System.out.println("[ " + numOfLikes + " like" + likesPlural + ", " + numOfDislikes + " dislike " + dislikesPlural +"]");

                // retrieving replies
                ArrayList<Reply> replies = ctrl.retrieveReplies(postID);
                List<Reply> latestReplies = replies.subList(Math.max(replies.size() - 3, 0), replies.size());
                int replyIndex = Math.max(1, replies.size() - 2);

                for (Reply r : latestReplies) {
                    System.out.println("   " + i + "." + replyIndex + " " + r.getUserID() + ": " + r.getContent());
                    replyIndex += 1;
                }

                System.out.println();
            }
        }
    }


    public void displayFriends(HashMap<Member, Boolean> friendsCommonMap) {
        Set<Member> keySet = friendsCommonMap.keySet();

        int counter = 1;

        for (Member member : keySet) {
            System.out.print("" + counter + ". " + member.getFullName());
            if (friendsCommonMap.get(member)) {
                System.out.print(" (Common friend)");
            }

            System.out.println();
            counter += 1;
        }

    }

    public boolean displayConfirm() {
        String choice;
        boolean validInput = false;
        while (!validInput) {
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.print("Are you sure? Y/N > ");
            choice = sc.nextLine();

            if (choice.equals("Y")) {
                return true;
            } else if (choice.equals("N")) {
                return false;
            } else {
                System.out.println("Please enter Y or N");
            }

        }
        return false;
    }

    public void displayGifts(HashMap<String, Integer> giftsMap) {
        ArrayList<String> seedArray = new ArrayList<>();
        Set<String> seedKeys = giftsMap.keySet();

        for (String string : seedKeys) {
            seedArray.add(string);
        }

        for (int index = 0; index < seedArray.size(); index ++) {
            System.out.println( index + 1 + "." + giftsMap.get(seedArray.get(index))  );
        } 
    }
}