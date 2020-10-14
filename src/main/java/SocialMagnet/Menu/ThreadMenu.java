package SocialMagnet.Menu;

/**
 * ThreadMenu
 */

import java.util.*;
import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

//import SocialMagnet.Control.ThreadCtrl;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Social.Post.Post;
import SocialMagnet.Control.ThreadCtrl;

public class ThreadMenu {
    private ThreadCtrl ctrl = new ThreadCtrl();

    public void displayThread(Post post, int index, Member user) {
        String choice;
        do {
            System.out.println();
            System.out.println("== Social Magnet :: View a Thread ==");
            
            // Retrieve thread and display here
            post.printPost(index);
            retrieveReplies(post, index);
            retrieveLikers(post);
            retrieveDislikers(post);
            
            System.out.println();
            Scanner sc = new Scanner(System.in);
            System.out.print("[M]ain | [K]ill | [R]eply | [L]ike | [D]islike > ");
            choice = sc.nextLine();
            switch (choice) {
                case "M":
                    break;
                case "K":
                    deletePost(user, post);
                    choice = "M";
                    WallMenu wallMenu = new WallMenu();
                    wallMenu.displayWall(user);
                    break;
                case "R":
                    replyPost(post, user);
                    break;
                case "L":
                    likePost(post, user, true, index);
                    break;
                case "D":
                    likePost(post, user, false, index);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M"));
    }
    
    public void likePost(Post post, Member user, boolean isLike, int index) {
        int postID = post.getPostID();
        String userID = user.getUserID();
        boolean isAdded = ctrl.likePost(postID, userID, isLike);
    }

    public void retrieveLikers(Post post) {
        int postID = post.getPostID();
        ArrayList<Member> likers = ctrl.retrieveLikers(postID);
        System.out.println();
        System.out.println("Who likes this post:");
        for (int i = 1; i <= likers.size(); i++) {
            Member member = likers.get(i-1);
            System.out.println("  " + i + ". " + member.getFullName() + " (" + member.getUserID() + ")");
        }
    }

    public void retrieveDislikers(Post post) {
        int postID = post.getPostID();
        ArrayList<Member> dislikers = ctrl.retrieveDislikers(postID);
        System.out.println();
        System.out.println("Who dislikes this post:");
        for (int i = 1; i <= dislikers.size(); i++) {
            Member member = dislikers.get(i-1);
            System.out.println("  " + i + ". " + member.getFullName() + " (" + member.getUserID() + ")");
        }
    }

    public void replyPost(Post post, Member user) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your reply > ");
        String message = sc.nextLine();
        int postID = post.getPostID();
        String userID = user.getUserID();
        boolean isAdded = ctrl.replyPost(postID, userID, message);
        if (isAdded) {
            System.out.println("Reply successfully posted.");
        }
    }

    public void retrieveReplies(Post post, int index) {
        // For this function, prof said to display all.
        int postID = post.getPostID();
        ArrayList<Reply> replies = ctrl.retrieveReplies(postID);
        int replyIndex = 1;

        for (Reply r : replies) {
            System.out.println("   " + index + "." + replyIndex + " " + r.getUserID() + ": " + r.getContent());
            replyIndex += 1;
        }
    }

    public void deletePost(Member user, Post post) {
        int postID = post.getPostID();
        String userID = user.getUserID();
        boolean isAdd = ctrl.deletePost(userID, postID);
        if (isAdd) {
            System.out.println("Post successfully deleted.");
        }
    }
}