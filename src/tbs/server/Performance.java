package tbs.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
 * Created by Nisarag on 7/04/2018.
 * The Performance class, it is an integral part of the TheatreBookingSystem since Performances are the main part
 * of the theatre and they relate to the classes Acts,Artists,Tickets,Theatres.
 *
 * param: performanceID - this is a String that contains the ID of the performance
 * param: actID - this is a String that contains the ID of the act
 * param: theatreID - this is a String that contains the ID of the theatre
 * param: startTimeStr - this is a String that contains the starting time of the performance in ISO8601 format
 * param: premiumPriceStr - this is a String that contains the price of the premium seats
 * param: cheapSeatsStr - this is a String that contains the price of the cheap seats
 *
 * Contains the usual getMethods, it also contains a method to create a unique performanceID from the field performanceIDCounter,
 * some utility methods if IDs already exist and also some error checking methods which are used when initializing an Performance object
 */

public class Performance {

    private String performanceID, actID, theatreID, startTimeStr, premiumPriceStr, cheapSeatsStr;
    private static long performanceIDCounter = 0;

    public Performance(String actID, String theatreID, String startTimeStr, String premiumPriceStr, String cheapSeatsStr){
        this.performanceID = createPerformanceID();
        this.actID = actID;
        this.theatreID = theatreID;
        this.startTimeStr = startTimeStr;
        this.premiumPriceStr = premiumPriceStr;
        this.cheapSeatsStr = cheapSeatsStr;
    }

    public String getPerformanceID(){
        return performanceID;
    }

    public String getActID(){
        return actID;
    }

    public String getTheatreID(){
        return theatreID;
    }

    public String getStartTime(){
        return startTimeStr;
    }

    public String getPremiumPrice(){
        return premiumPriceStr.substring(1,premiumPriceStr.length());
    }

    public String getCheapPrice(){
        return cheapSeatsStr.substring(1,cheapSeatsStr.length());
    }

    public String createPerformanceID() {
        return "P" + String.valueOf(performanceIDCounter++);
    }

    public boolean nullActIDCheck(String actID) {
        return (actID == null || actID.equals(""));
    }

    public boolean nullTheatreIDCheck(String theatreID) {
        return (theatreID == null || theatreID.equals(""));
    }

    public boolean nullCheapPriceCheck(String cheapSeatsStr) {
        return (cheapSeatsStr == null || cheapSeatsStr.equals(""));
    }

    public boolean formatCheapPriceCheck(String cheapSeatsStr) {
        return !(cheapSeatsStr.substring(0,1).equals("$")); // check if first char is dollar sign
    }

    public boolean parseCheapPriceCheck(String cheapSeatsStr) {
        cheapSeatsStr = cheapSeatsStr.substring(1); // grab the number after dollar sign
        try {
            int cheapSeatsPrice = Integer.parseInt(cheapSeatsStr); // check when you parse an int it doesn't throw an exception
        } catch (NumberFormatException e){
            return true;
        }
        return false;
    }

    public boolean nullPremiumPriceCheck(String premiumPriceStr) {
        return (premiumPriceStr == null || premiumPriceStr.equals(""));
    }

    public boolean formatPremiumPriceCheck(String premiumPriceStr) {
        return !(premiumPriceStr.substring(0,1).equals("$")); // check if first char is dollar sign
    }

    public boolean parsePremiumPriceCheck(String premiumPriceStr) {
        premiumPriceStr = premiumPriceStr.substring(1);
        try {
            int premiumSeatsPrice = Integer.parseInt(premiumPriceStr); // check when you parse an int it doesn't throw an exception
        } catch (NumberFormatException e){
            return true;
        }
        return false;
    }

    public boolean nullTimeCheck(String startTimeStr) {
        return (startTimeStr == null || startTimeStr.equals(""));
    }

    public boolean formatTimeCheck(String startTimeStr) {
        if (!(startTimeStr.length() == 16)) { // checks that the length is equal to 16, if it is not then return because then it is not in ISO format
            return true;
        }

        if (startTimeStr.substring(11,13).equals("24")) { // If a 24hr time is inserted then it is valid so it should be allowed
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm"); // check date is in correct ISO format if it is not, throw an exception
        try {
            dateFormat.parse(startTimeStr);
            dateFormat.setLenient(false);
        } catch (ParseException p) {
            return true;
        }

        return false;
    }

    public boolean theatreIDExists(ArrayList<Theatre> theatreList, String theatreID){ // check if a theatreExists relating to its theatreID
        for (Theatre t : theatreList) {
            if (t.getTheatreId().equals(theatreID)) {
                return true;
            }
        }
        return false;
    }

    public boolean actIDExists(ArrayList<Act> actList, String actID) { // checks if an Act exists relating to its actID
        for (Act a : actList) {
            if (a.getActID().equals(actID)) {
                return true;
            }
        }
        return false;
    }

}