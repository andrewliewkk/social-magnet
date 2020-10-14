package SocialMagnet.Control;

/**
 * Test Farmland
 */

import org.junit.jupiter.api.*;
import java.util.*;
import java.sql.SQLException;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Farm.*;
import SocialMagnet.Exceptions.FarmlandException;
import SocialMagnet.Exceptions.StealException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFarmland {
    private MemberDAO memberDAO = new MemberDAO();
    private PlotDAO plotDAO = new PlotDAO();
    private CropDAO cropDAO = new CropDAO();
    private PlotCropDAO plotCropDAO = new PlotCropDAO();
    private FarmlandCtrl farmLandCtrl = new FarmlandCtrl();
    private FriendFarmCtrl friendFarmCtrl = new FriendFarmCtrl();
    private PlotCropStealDAO plotCropStealDAO = new PlotCropStealDAO();

    Crop testPapaya = cropDAO.retrieveCropByName("Papaya");

    @BeforeAll
    public void resetUser() {

    }

    @Test
    public void testHarvest() {
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
        
        // tester2 will try to steal from tester - steal will be successful
        Map<String, Integer> stealResult = null;
        try {
            stealResult = friendFarmCtrl.steal(testUser2, testUser);
        } catch (StealException e) {
            System.out.println(e.getMessage());
        }

        // Check the steal amount was rightly entered
        int stealAmt = plotCropStealDAO.calculatePlotCropSteals(testUser, 2);
        Assertions.assertTrue(stealAmt == stealResult.get("Papaya"));

        // Check if steal affected the yield
        int expectedYield = testPlotCrop.getYield();

        Map<String, Map<String, Integer>> harvestResult = farmLandCtrl.harvestAll(testUser);
        Map<String, Integer> testPapayaResult = harvestResult.get("Papaya");
        int gainedYield = testPapayaResult.get("yield");
        int gainedXP = testPapayaResult.get("xp");
        int gainedGold = testPapayaResult.get("gold");

        Assertions.assertEquals(gainedYield, expectedYield - stealAmt);

        int cropGold = testPapaya.getSalePrice();
        int cropXP = testPapaya.getXP();
        System.out.println(gainedGold);

        Assertions.assertEquals(gainedXP, cropXP * gainedYield);
        Assertions.assertEquals(gainedGold, cropGold * gainedYield);

    }


    @Test
    public void testClearCrop() {
        int plotIDTest = 1;
        String[] memberInfo = {"tester", "password", "Tester"};
        memberDAO.addUser(memberInfo[0], memberInfo[1],
                memberInfo[2]);
        Member tester = memberDAO.getUser(memberInfo[0]);

        plotDAO.createNewPlots("tester", "Novice");
        plotCropDAO.plantCrop(tester, plotIDTest, testPapaya);

        PlotCrop testPlotCrop = plotCropDAO.getPlotCropByID("tester", plotIDTest);

        // Given a time of 90 minutes, our papaya should have matured
        Calendar c = Calendar.getInstance();
        c.setTime(testPlotCrop.getTimePlanted());
        c.add(Calendar.MINUTE, -90);

        plotCropDAO.setNewTimePlanted( tester, plotIDTest, c.getTime());

        // Set the tester's gold to 10
        tester.setGold(10);

        farmLandCtrl.clearCrop(tester, testPlotCrop);

        Assertions.assertEquals(5, tester.getGold());

    }


    @Test
    public void testClearCropNoGold() {
        int plotIDTest = 1;
        String[] memberInfo = {"tester2", "password", "Tester2"};
        memberDAO.addUser(memberInfo[0], memberInfo[1],
                memberInfo[2]);
        Member tester = memberDAO.getUser(memberInfo[0]);

        plotDAO.createNewPlots("tester2", "Novice");
        plotCropDAO.plantCrop(tester, plotIDTest, testPapaya);

        PlotCrop testPlotCrop = plotCropDAO.getPlotCropByID("tester2", plotIDTest);

        // Given a time of 90 minutes, our papaya should have wilted
        Calendar c = Calendar.getInstance();
        c.setTime(testPlotCrop.getTimePlanted());
        c.add(Calendar.MINUTE, -90);

        plotCropDAO.setNewTimePlanted( tester, plotIDTest, c.getTime());

        // Set the tester's gold to 0
        memberDAO.setGold(0, "tester2");

        Exception e = Assertions.assertThrows(FarmlandException.class, () ->
            farmLandCtrl.clearCrop(tester, testPlotCrop));
        Assertions.assertEquals("Insufficient gold!", e.getMessage());
    }
    
    @AfterAll
    public void removeUser() {

    String[] memberTests = {"tester", "tester2"};

        for (String testID : memberTests) {
            Member testUser = memberDAO.getUser(testID);
            if (testUser != null) {
                memberDAO.deleteUser(testID);
            } 
        }
  
    }
}