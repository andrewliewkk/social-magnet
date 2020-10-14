package SocialMagnet.Control;

/**
 * GiftCtrl
 */

import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;
import SocialMagnet.Utility.*;
import SocialMagnet.Exceptions.GiftException;
import SocialMagnet.Farm.*;

public class GiftCtrl {
    private MemberDAO memberDAO = new MemberDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private GiftDAO giftDAO = new GiftDAO();

    // This method takes in the Member who is sending the gift, the String that can have multiple users and the seed they are sending
    public void sendToFriends(Member sender, String nameString, Crop seed) throws GiftException {
        ArrayList<Member> friendsToSendGiftTo = new ArrayList<>();
        List<String> namesList = Arrays.asList(nameString.split(","));
        ArrayList<Member> friendsOfUser = memberDAO.getFriends(sender.getUserID());

        // Checks if sender has already sent 5 gifts
        if (giftDAO.hasExceededFiveToday(sender)) {
            throw new GiftException("Cannot send more than 5 gifts a day!");
        }

        for (String name : namesList) {
            for (Member friend : friendsOfUser) {
                if (sender.getUserID().equals(name)) {
                    throw new GiftException("Cannot send gift to yourself!");
                }

                if (friend.getUserID().equals(name)) {
                    friendsToSendGiftTo.add(friend);       
                }
            }
        }

        // If one of the friends in the String of friends given does not match one of the member's friends, 
        // an exception will be thrown and none of the friends will receive the gift.
        if (friendsToSendGiftTo.size() != namesList.size()) {
            throw new GiftException("Your input(s) did not match any of your friends.");
        }

        for (Member friend : friendsToSendGiftTo) {
            // Check if possible to send gift
            if (giftDAO.hasSentToFriendToday(sender, friend)) {
                throw new GiftException("Cannot send to the same friend on the same day!");
            }
            giftDAO.sendGift(sender, friend, seed);
        }
    }

    public void acceptGifts(Member user) throws GiftException{
        ArrayList<Gift> gifts = giftDAO.acceptGifts(user);
        // If  the size of accepted gifts is 0, this would mean that all the gifts has been accepted
        if (gifts == null || gifts.size() == 0) {
            throw new GiftException("You have claimed all your gifts.");
        }
        inventoryDAO.processGifts(user, gifts);
    }
}