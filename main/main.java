package main;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import util.CurrentStatus;
import util.LocationValidator;
import util.Banker;
import util.LeatherType;
import util.Tanner;


/**
 * Banks, Tans and Travels. The Ultimate F2P Bot Tanner
 */

@ScriptManifest(author = "CheeseQueso", category = Category.MONEYMAKING, description = "Tans hides (soft or hard) in AlKharid, then walks to GE", name = "QuesoTanner", version = 1.0)
public class main extends AbstractScript{

    Area alkharidBank = new Area(3269, 3161, 3271, 3170, 0);
    Area tannerArea = new Area(3271, 3189, 3276, 3194, 0);

    LocationValidator initializer = new LocationValidator(this);
    Banker bank = new Banker(this);
    Tanner tanner = new Tanner(this);

    //Determines the type of leather you want and the cost
    private LeatherType tanStatus;

    //Current status in script traversal
    private CurrentStatus currentStatus;

    /** Set the initial values */
    @Override
    public void onStart() {

        log("Initializing Hide Tanner");

        //Type of leather you want to tan
        tanStatus = LeatherType.HARD_LEATHER;

        //Current script status
        currentStatus = CurrentStatus.INITIALIZING;

        super.onStart();
    }

    /** Do this over and over again until base condition is met */
    @Override
    public int onLoop() {

        switch (currentStatus) {

            case INITIALIZING:
                log("We are initializing");
                boolean insideBank = initializer.insideBankingArea(alkharidBank, tannerArea, getLocalPlayer());
                if (insideBank) { currentStatus = CurrentStatus.BANKING; }
                break;
            case BANKING:
                log("We are banking");
                handleBanking();
                break;
            case TRAVEL:
                log("We are travelling");
                handleTravel();
                break;
            case TAN:
                log("We are tanning");
                handleTanning();
                break;
            default:
                log("Default case");
                break;
        }

        return Calculations.random(500, 2000);
    }

    /** Handle all the logic for withdrawing and depositing goods */
    private void handleBanking() {

        bank.bankBanker();
        bank.depositAll();
        bank.checkSufficientFunds(tanStatus);
        bank.withdrawGold(tanStatus);
        bank.withdrawHides();
        currentStatus = CurrentStatus.TRAVEL;
    }

    /** Handle all the logic for traveling to and from the tanner NPC */
    private void handleTravel() {

        //Travel to tanner
        initializer.walkToTanner(tannerArea);

        NPC desertTanner = getNpcs().closest(tanner -> tanner != null && tanner.hasAction("Trade"));

        //Checks to see if the door to tanner is present and open, if not open it
        tanner.handleDoorOutside();

        //Ensure the tanner is present and the player is in the tanning area
        if (desertTanner.isOnScreen() && initializer.insideTanningArea(tannerArea, getLocalPlayer() == true)) {
            currentStatus = CurrentStatus.TAN;
        }
    }

    /** Handle all the logic for tanning the inventory hides */
    private void handleTanning() {

        //Trade the dude
        tanner.initiateTrade();

        //Tan all the hides based on widget-type
        if (tanStatus.getLeatherType().contains("soft")) {
            tanner.tanAllHides(124);

        } else if (tanStatus.getLeatherType().contains("hard")) {
            tanner.tanAllHides(125);
        }

        //See if the process completed successfully
        if (tanner.checkHidesTanned()) {

            //Handle if the door is closed from the inside
            if (tanner.handleDoorInside() == true) {

                //Restart the work-flow
                currentStatus = CurrentStatus.INITIALIZING;
            }
        }
    }
}