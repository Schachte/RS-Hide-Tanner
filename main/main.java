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



/**
 * Banks, Tans and Travels. The Ultimate F2P Bot Tanner
 */

@ScriptManifest(author = "CheeseQueso", category = Category.MONEYMAKING, description = "Tans hides like a beast", name = "tanner", version = 1.0)
public class main extends AbstractScript{

    Area alkharidBank = new Area(3269, 3161, 3271, 3170, 0);
    Area tannerArea = new Area(3271, 3189, 3276, 3194, 0);

    LocationValidator initializer = new LocationValidator(this);
    Banker bank = new Banker(this);

    //Determines the type of leather you want and the cost
    private LeatherType tanStatus;

    //Current status in script traversal
    private CurrentStatus currentStatus;



    @Override
    public void onStart() {

        log("Initializing Hide Tanner");

        //Type of leather you want to tan
        tanStatus = LeatherType.HARD_LEATHER;

        //Current script status
        currentStatus = CurrentStatus.INITIALIZING;

        super.onStart();
    }

    @Override
    public int onLoop() {

        switch (currentStatus) {

            case INITIALIZING:
                log("We are initializing");
                boolean insideBank = initializer.validateLocation(alkharidBank, tannerArea, getLocalPlayer());
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
        //TO:DO Need to handle closed door
        initializer.walkToTanner(tannerArea);

        NPC desertTanner = getNpcs().closest(tanner -> tanner != null && tanner.hasAction("Trade"));

        if (desertTanner.isOnScreen()) {
            currentStatus = CurrentStatus.TAN;
        }
    }

    /** Handle all the logic for tanning the inventory hides */
    private void handleTanning() {

        //Get the banker in alkharid
        NPC desertTanner = getNpcs().closest(tanner -> tanner != null && tanner.hasAction("Trade"));

        //Tan Tanner
        if (desertTanner != null) {
            log("Attempting to trade");
            desertTanner.interact("Trade");
            sleepUntil(() -> getWidgets().getWidgetChild(324, 125).isVisible(), 3000);
        } else {
            log("Tanner is null ERROR");
        }

        if (getWidgets().getWidgetChild(324, 125) !=null){
            getWidgets().getWidgetChild(324, 125).interact("Tan All");
            log("making");
        }
        else {
            log("widget is null");
        }
        currentStatus = CurrentStatus.INITIALIZING;

    }
}