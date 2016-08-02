package util;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.NPC;


/**
 * Class responsible for handling all banking related activities during the tanning process
 */
public class Banker {

    public AbstractScript script;

    public Banker(AbstractScript script) {
        this.script = script;
    }

    public void bankBanker() {

        //Get the banker in alkharid
        NPC desertBanker = script.getNpcs().closest(banker -> banker != null && banker.hasAction("Bank"));

        //Open bank
        if (desertBanker != null) {
            desertBanker.interact("Bank");
        }
    }

    /** Deposits all items in inventory into the player bank */
    public void depositAll() {

        script.log("Depositing Items - [KEEPING GOLD IN INVENTORY]");

        //Ensure bank is open
        if (script.sleepUntil(() -> script.getBank().isOpen(), 9000)) {

            script.log("Bank is open");

            //Deposit everything into the bank
            script.getBank().depositAllExcept(coins -> coins != null && coins.getName().contains("Coins"));
        }
    }

    /** Check to see if the player has any more hides */
    public boolean checkHidesExistInBank() {

        //TO-DO - FINISH THIS FUNCTION

        if(script.getBank().contains(hides -> hides != null && hides.getName().contains("Cowhide"))) {
            script.log("we have more hides!");
            return true;
        } else {
            script.log("we are out of hides!");
            return false;
        }

    }

    /** Checks if there is enough GP to tan the hide count based on the type specified */
    public boolean checkSufficientFunds(LeatherType leather) {

        //Number of cowhides the user posesses
        int cowHideCount = script.getBank().count(1739);

        //Total number of coins existent within your inventory
        int inventoryCoins = script.getInventory().count(995);

        //Total number of coins existent within your bank
        int bankCoins = script.getBank().count(995);

        //Total cost to spend tanning these hides
        int totalCost = -1;

        //Total cost to spend tanning these hides
        if (leather.getLeatherType().contains("soft")) {
            script.log("Tanning soft leather");
            totalCost = cowHideCount*leather.getLeatherCost();
        }
        else if (leather.getLeatherType().contains("hard")) {
            script.log("Tanning hard leather");
            totalCost = cowHideCount*leather.getLeatherCost();
        }
        else if (totalCost == -1) {
            script.log("Errored!");
        }

        if (inventoryCoins >= totalCost || bankCoins >= totalCost) {
            script.log("You have enough money to tan all these hides");
            script.getBank().depositAllExcept(items -> items != null && items.getName().contains("Coins"));
            return true;
        } else {
            script.log("You do not have enough money to tan all these hides");
            return false;
        }
    }

    /** Withdraws the amount of gold required to tan the hides */
    public void withdrawGold(LeatherType leather) {

        //Number of cowhides the user posesses
        int cowHideCount = script.getBank().count(1739);
        int totalCost = -1;

        //Total cost to spend tanning these hides
        if (leather.getLeatherType().contains("soft")) {
            script.log("Tanning soft leather");
            totalCost = cowHideCount*leather.getLeatherCost();

            //TO:DO - Add in the math later if we're doing sufficientFundChecking
            //script.getBank().withdraw(coins -> coins != null && coins.getName().contains("Coins"), totalCost);

            script.getBank().withdraw(coins -> coins != null && coins.getName().contains("Coins"), 5000000);
            script.sleepUntil(() -> script.getInventory().contains(coins -> coins != null && coins.getName().contains("Coins")), 3000);
        }
        else if (leather.getLeatherType().contains("hard")) {
            script.log("Tanning hard leather");
            totalCost = cowHideCount*leather.getLeatherCost();

            //TO:DO - Add in the math later if we're doing sufficientFundChecking
            //script.getBank().withdraw(coins -> coins != null && coins.getName().contains("Coins"), totalCost);

            script.getBank().withdraw(coins -> coins != null && coins.getName().contains("Coins"), 5000000);
            script.sleepUntil(() -> script.getInventory().contains(coins -> coins != null && coins.getName().contains("Coins")), 3000);

        }
        else if (totalCost == -1) {
            script.log("Errored!");
        }
    }

    /** Withdraws max number of hides from the bank into inventory */
    public void withdrawHides() {

        //Get all your hides in the inventory to prep for tanning
        script.getBank().withdrawAll(hides -> hides != null && hides.getName().contains("Cowhide"));
    }
}