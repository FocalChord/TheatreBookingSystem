package tbs.server;

import java.util.ArrayList;

/*
 * Created by Nisarag on 7/04/2018.
 * The Theatre class, it is an integral part of the TheatreBookingSystem since there are going to be a lot of theatres
 * and each performance will be in a theatre so it is quite important
 * param: theatreID - this is a String that contains the ID of the theatre
 * param: seatingDimension - this is an integer which indicates the dimension of the theatre
 * param: floorArea - this is an integer which indicates the floorArea of the theatre
 */

public class Theatre {
    private String theatreId;
    private int seatingDimension, floorArea;

    public Theatre(String theatreId, int seatingDimension, int floorArea) {
        this.theatreId = theatreId;
        this.seatingDimension = seatingDimension;
        this.floorArea = floorArea;
    }

    public String getTheatreId() {
        return theatreId;
    }

    public int getSeatingDimension() {
        return seatingDimension;
    }

    public ArrayList<String> getAvailableSeats(ArrayList<Ticket> ticketList,ArrayList<String> seatsAvailableList,  String performanceID) {

        // This method goes finds the available seats for a theatre object by looping through all the rows and seats
        // and checking each ticket to see if it matches and then if there are free seats found (no ticket allocated yet)
        // then add to the list of available seats

        for (int row = 1; row <= seatingDimension; row++) {
            for (int seat = 1; seat <= seatingDimension; seat++) {
                boolean found = false;
                for (Ticket t : ticketList) {
                    if (t.getPerformanceID().equals(performanceID) && t.getRowNumber() == row && t.getSeatNumber() == seat) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    seatsAvailableList.add(row + "\t" + seat);
                }
            }
        }

        return seatsAvailableList;
    }

}
