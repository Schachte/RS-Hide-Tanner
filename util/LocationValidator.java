package util;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.Player;


/**
 * Validates player location
 */
public class LocationValidator{

    public AbstractScript script;

    public LocationValidator(AbstractScript script) {
        this.script = script;
    }

    /** Check to see where you are */
    public boolean validateLocation(Area bankArea, Area tanArea, Player currentPlayerLocation) {

        if (!bankArea.contains(currentPlayerLocation)) {
            walkToBank(bankArea);
            return false;
        } else {
            return true;
        }
    }

    public boolean insideTanningArea(Area tanArea, Player currentPlayerLocation) {

        if (tanArea.container(currentPlayerLocation)) {
            return true;
        } else {
            return false;
        }
    }

    /** Go to the bank */
    public void walkToBank(Area bankLocation) {

        script.getWalking().walk(bankLocation.getRandomTile());
    }

    /** Go to the tanner */
    public void walkToTanner(Area tannerLocation) {

        script.getWalking().walk(tannerLocation.getRandomTile());

    }
}
