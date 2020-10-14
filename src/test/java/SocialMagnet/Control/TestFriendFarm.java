package SocialMagnet.Control;

/**
 * Test FriendFarm
 */

import org.junit.jupiter.api.*;
import java.util.*;
import java.sql.SQLException;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Farm.*;
import SocialMagnet.Exceptions.StealException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFriendFarm {
    private MemberDAO memberDAO = new MemberDAO();
    private PlotDAO plotDAO = new PlotDAO();
    private CropDAO cropDAO = new CropDAO();
    private PlotCropDAO plotCropDAO = new PlotCropDAO();
    private FriendCtrl friendCtrl = new FriendCtrl();
    private FriendFarmCtrl friendFarmCtrl = new FriendFarmCtrl();

    String[][] testMembers = {  {"tester", "password", "Tester"} ,
                                {"tester2", "password", "Tester2"} , 
                                {"tester3", "password", "Tester3"} ,          
                            };

    Crop testPapaya = cropDAO.retrieveCropByName("Papaya");
    // A papaya's mature time is 30 minutes.

    @BeforeAll
    public void resetUser() {

        // Populate members using memberDAO
        for (int index = 0; index < testMembers.length; index ++ ) {
            String[] memberInfo = testMembers[index];
            if (memberDAO.getUser(memberInfo[0]) == null) {
                memberDAO.deleteUser(memberInfo[0]);
            }
            memberDAO.addUser(memberInfo[0], memberInfo[1],
                    memberInfo[2]);
        } 

        // Make tester and tester 2 friends
        friendCtrl.sendFriendRequest("tester", "tester2");

        try {
            friendCtrl.acceptFriendRequest("tester", "tester2");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }

    @AfterEach
    public void removeCrops() {
        Member testUser = memberDAO.getUser("tester");
        
        for(int plotNo = 1; plotNo <= 5; plotNo ++) {
            PlotCrop testPlotCrop = plotCropDAO.getPlotCropByID("tester", plotNo);
            if (testPlotCrop != null) {
                plotCropDAO.removePlotCrop(testUser, plotNo, false);
            }
        }
    }

    @Test
    public void testSteal() {
        int plotIDTest = 1;
        // Give tester a plot
        Member testUser = memberDAO.getUser("tester");
        Member testUser2 = memberDAO.getUser("tester2");

        plotDAO.createNewPlots("tester", "Novice");
        plotCropDAO.plantCrop(testUser, plotIDTest, testPapaya);
        PlotCrop testPlotCrop = plotCropDAO.getPlotCropByID("tester", plotIDTest);

        // Given a time of 45 minutes, our papaya should mature
        Calendar c = Calendar.getInstance();
        c.setTime(testPlotCrop.getTimePlanted());
        c.add(Calendar.MINUTE, -45);

        plotCropDAO.setNewTimePlanted( testUser, plotIDTest, c.getTime());

        // tester2 will try to steal from tester
        try {
            Map<String, Integer> stealResult = friendFarmCtrl.steal(testUser2, testUser);
            int stealAmount = stealResult.get("Papaya");
            Assertions.assertTrue( (stealAmount >= 0.01 * testPapaya.getMinYield() &&
                    stealAmount <= 0.05 * testPapaya.getMaxYield()) );
        } catch (StealException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testSamePersonSteal() {
        int plotIDTest = 2;
        // Give tester a plot
        Member testUser = memberDAO.getUser("tester");
        Member testUser2 = memberDAO.getUser("tester2");

        plotDAO.createNewPlots("tester", "Novice");
        plotCropDAO.plantCrop(testUser, plotIDTest, testPapaya);
        PlotCrop testPlotCrop = plotCropDAO.getPlotCropByID("tester", plotIDTest);

        // Given a time of 45 minutes, our papaya should mature
        Calendar c = Calendar.getInstance();
        c.setTime(testPlotCrop.getTimePlanted());
        c.add(Calendar.MINUTE, -45);

        plotCropDAO.setNewTimePlanted( testUser, plotIDTest, c.getTime());
        
        // tester2 will try to steal from tester
        try {
            Map<String, Integer> stealResult = friendFarmCtrl.steal(testUser2, testUser);
        } catch (StealException e) {
            System.out.println(e.getMessage());
        }

        // tester2 tries to steal from tester a 2nd time
        Exception e = Assertions.assertThrows(StealException.class, () ->
                friendFarmCtrl.steal(testUser2, testUser));
        Assertions.assertEquals("You already stole from tester!", e.getMessage());
    }

    @Test
    public void testManyPersonSteal() {
        int plotIDTest = 2;
        // Give tester a plot
        Member testUser = memberDAO.getUser("tester");

        plotDAO.createNewPlots("tester", "Novice");
        plotCropDAO.plantCrop(testUser, plotIDTest, testPapaya);
        PlotCrop testPlotCrop = plotCropDAO.getPlotCropByID("tester", plotIDTest);

        // Given a time of 45 minutes, our papaya should mature
        Calendar c = Calendar.getInstance();
        c.setTime(testPlotCrop.getTimePlanted());
        c.add(Calendar.MINUTE, -45);

        plotCropDAO.setNewTimePlanted( testUser, plotIDTest, c.getTime());
        
        // Create members and steal from tester
        boolean multStealSuccess = false;

        // Starting from 4 and ends at 24. This error should be captured by 20 steals.
        for(int trialSteal = 4; trialSteal < 24; trialSteal ++) { 

            // Create fake member
            String[] memberInfo = {"tester" + trialSteal, "password", "Tester" + trialSteal};
            if (memberDAO.getUser(memberInfo[0]) == null) {
                memberDAO.deleteUser(memberInfo[0]);
            }
            memberDAO.addUser(memberInfo[0], memberInfo[1],
                    memberInfo[2]);

            // Try to steal multiple times
            try {
                Member testStealer = memberDAO.getUser("tester" + trialSteal);
                Map<String, Integer> stealResult = friendFarmCtrl.steal(testStealer, testUser);
            } catch (StealException e) {
                multStealSuccess = true;
                // The last person to grab the last item will yield 0 items,
                // as the remaining % to steal is too small.
                Assertions.assertEquals("Steal yielded 0 items. Unlucky!", e.getMessage());
                break;
            }
        }

    }

    @AfterAll
    public void removeUser() {

        for (String[] memberInfo : testMembers) {
            String testID = memberInfo[0];
            Member testUser = memberDAO.getUser(testID);
            if (testUser != null) {
                memberDAO.deleteUser(testID);
            } 
        }
  
    }
}
