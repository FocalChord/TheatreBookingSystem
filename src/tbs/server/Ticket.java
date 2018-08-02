package tbs.server;

/*
 * Created by Nisarag on 7/04/2018.
 * The Act class, it is an integral part of the TheatreBookingSystem since every performance requires a ticket system
 * to ensure that there is no overflow and issuing tickets occurs successfully
 *
 * param: performanceID - this is a String that contains the ID of the performance
 * param: rowNumber - this is an Integer which indicates the row number for the Ticket
 * param: seatNumber - this is an integer which indicates the seat number for the Ticket
 * param: theatreID - this is a String that contains the ID of the corresponding theatre to the performance
 *
 * Contains the usual getMethods, it also contains a method to create a unique ticketID from the field ticketIDCounter, and
 * also some error checking methods which are used when initializing an Ticket object
 */

public class Ticket {

    private String ticketID, performanceID;
    private int rowNumber, seatNumber;
    private static long ticketIDCounter = 0;

    public Ticket(String performanceID, int rowNumber, int seatNumber){
        this.ticketID = createTicketID();
        this.performanceID = performanceID;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public String getTicketID(){
        return ticketID;
    }

    public String getPerformanceID(){
        return performanceID;
    }

    public int getRowNumber(){
        return rowNumber;
    }

    public int getSeatNumber(){
        return seatNumber;
    }

    public String createTicketID() {
        return "TI" + String.valueOf(ticketIDCounter++);
    }

}
