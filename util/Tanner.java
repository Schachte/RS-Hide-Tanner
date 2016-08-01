package util;

/**
 * Handles the tanner actions
 */
public class Tanner {

    public AbstractScript script;

    public Tanner(AbstractScript script) {
        this.script = script;
    }

    /** Handles the closed door from outside the tanning salon */
    public boolean handleDoorOutside() {

        //Check if door ID exists
        //When door does exist, check if it's closed
        //If it's closed, then open it
            //Wait to test if door is open
            //Then walk inside

        //If player location inside tannerArea is true
            //Return true

        //Else
            //Return false

    }

    /** Handles the closed door from inside the tanning salon */
    public boolean handleDoorInside() {

        //Check if door ID exists
        //When door does exist, check if it's closed
        //If it's closed, then open it
            //Wait to test if door is open
            //Then walk outside

        //If player location NOT inside tanner area
            //Return true

        //Else
           //Return false
    }

    /** Function to ensure tanning is complete */
    public boolean checkHidesTanned() {

        //Check that you have tanned hide in your inventory
        //If you do
            //Return true
        //Else
            //Return false
    }

    /** Initiate leather trading with the dude */
    public void initiateTrade(int child_widget_int) {

        //Get the desert tanner dude
        //TO:DO add this into a try-catch block
        NPC desertTanner = getNpcs().closest(tanner -> tanner != null && tanner.hasAction("Trade"));

        //Tan Tanner
        if (desertTanner != null) {
            log("Attempting to trade");
            desertTanner.interact("Trade");
            sleepUntil(() -> getWidgets().getWidgetChild(324, child_widget_int).isVisible(), 3000);
        } else {
            log("Tanner is null ERROR");
        }
    }

    /** Tan all the hides in the player inventory */
    public void tanAllHides(int child_widget_int) {

        if (getWidgets().getWidgetChild(324, child_widget_int) !=null){
            getWidgets().getWidgetChild(324, child_widget_int).interact("Tan All");
            log("making");
        } else {
            log("widget is null");
        }
    }

}
