package tbs.server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TBSServerImpl implements TBSServer {

    private Database database = new Database();

    public String initialise(String path) {
        if (path == null || path.equals("")) {
            throw new RuntimeException("ERROR Path is empty");
        } // Null check for path

        Scanner input = null;
        Path filePath = Paths.get(path); // Double checking if it is the correct path

        try {
            input = new Scanner(filePath);
        } catch (IOException ioExc) {
            return "File does not exist or is not readable";
        }

        while (input.hasNext()) {
            try {
                String[] theatreInfo = input.nextLine().split("\t"); // Splits all the csv file by tab character
                if (theatreInfo.length == 4) { // Checks if there are 4 things from the CSV file
                    database.setTheatreList(theatreInfo[1], Integer.parseInt(theatreInfo[2]), Integer.parseInt(theatreInfo[3])); // We only want to store the latter 3 pieces of information
                }
            } catch (NumberFormatException e) {
                return "Invalid format"; // If the parsing fails then a number format exception is thrown since it is not the correct format.
            }
        }

        input.close(); // safely close scanner

        return "";
    }

    public List<String> getTheatreIDs() {
        ArrayList<String> theatreIDList = new ArrayList<>();
        return database.queryTheatreIDs(theatreIDList); // asks the database for list of sorted theatreIds
    }

    public List<String> getArtistIDs() {
        ArrayList<String> artistIDList = new ArrayList<>();
        return database.queryArtistIDs(artistIDList); // asks the database for list of sorted artistIDs
    }

    public List<String> getArtistNames() {
        ArrayList<String> artistNameList = new ArrayList<>();
        return database.queryArtistNames(artistNameList); // asks the database for list of sorted artistNames
    }

    public List<String> getActIDsForArtist(String artistID) {
        ArrayList<String> actIDsList = new ArrayList<>();

        if (artistID == null || artistID.equals("")) { // checks artistName for empty string
            actIDsList.add("ERROR Artist ID is empty");
            return actIDsList;
        }

        return database.queryActIDs(actIDsList, artistID); // asks the database for list of sorted actIds
    }

    public List<String> getPeformanceIDsForAct(String actID) {
        ArrayList<String> performanceIDsList = new ArrayList<>();

        if (actID == null || actID.equals("")) {  // checks actID for empty string
            performanceIDsList.add("ERROR act ID is empty");
            return performanceIDsList;
        }

        return database.queryPerformanceIDs(performanceIDsList, actID); // asks the database for list of sorted performanceIds
    }

    public List<String> getTicketIDsForPerformance(String performanceID) {
        ArrayList<String> ticketIDsList = new ArrayList<>();

        if (performanceID == null || performanceID.equals("")) { // checks performanceID for empty string
            ticketIDsList.add("ERROR Performance ID is empty");
            return ticketIDsList;
        }

        return database.queryTicketIds(ticketIDsList, performanceID); // asks the database for list of sorted ticketIds
    }

    public String addArtist(String name) {
        Artist newArtist = new Artist(name);

        if (newArtist.nullArtistNameCheck(name)) { // null check for artist
            return "ERROR Name is empty";
        } else if (newArtist.duplicateArtistNameCheck(database.getArtistList(), name)) { // checks if artistName already exists in database
            return "ERROR Name already exists in database";
        }

        return database.addToArtistList(newArtist); // if there are no errors then ask database to add artist to artistList
    }

    public String addAct(String title, String artistID, int minutesDuration) {

        Act newAct = new Act(title, artistID, minutesDuration);

        if (newAct.nullTitleCheck(title)) { // basic null checks for artist id and title
            return "ERROR Title is empty";
        } else if (newAct.nullArtistIDCheck(artistID)) {
            return "ERROR Artist ID is empty";
        } else if (newAct.minutesCheck(minutesDuration)) { // check legibility of minutes
            return "ERROR Minutes cannot be zero or negative";
        } else if (!(newAct.artistIDExists(database.getArtistList(), artistID))) { // checks if artistID exists in database
            return "ERROR No artist with ID " + artistID + " exists";
        }

        return database.addToActList(newAct); // if there are no errors then ask database to add act to actList
    }

    public String schedulePerformance(String actID, String theatreID, String startTimeStr, String premiumPriceStr, String cheapSeatsStr) {
        Performance newPerformance = new Performance(actID, theatreID, startTimeStr, premiumPriceStr, cheapSeatsStr);


        // going through all the necessary checking before adding performance (most of the information of what the checking does is in the name)
        if (newPerformance.nullActIDCheck(actID)) {
            return "ERROR Act ID is empty";
        } else if (newPerformance.nullTheatreIDCheck(theatreID)) {
            return "ERROR Theatre ID is empty";
        } else if (newPerformance.nullCheapPriceCheck(cheapSeatsStr)) {
            return "ERROR Cheap price is empty";
        } else if (newPerformance.formatCheapPriceCheck(cheapSeatsStr)) {
            return "ERROR Cheap price requires a dollar sign at the start";
        } else if (newPerformance.parseCheapPriceCheck(cheapSeatsStr)) {
            return "ERROR Cheap price requires a integer number after the dollar sign";
        } else if (newPerformance.nullPremiumPriceCheck(premiumPriceStr)) {
            return "ERROR Premium price is empty";
        } else if (newPerformance.formatPremiumPriceCheck(premiumPriceStr)) {
            return "ERROR Premium price requires a dollar sign at the start";
        } else if (newPerformance.parsePremiumPriceCheck(premiumPriceStr)) {
            return "ERROR Premium price requires a integer number after the dollar sign";
        } else if (newPerformance.nullTimeCheck(startTimeStr)) {
            return "ERROR Start time is empty";
        } else if (newPerformance.formatTimeCheck(startTimeStr)) {
            return "ERROR Incorrect time format";
        } else if (!(newPerformance.theatreIDExists(database.getTheatreList(), theatreID))) {
            return "ERROR No Theatre with ID " + theatreID + " exists";
        } else if (!(newPerformance.actIDExists(database.getActList(), actID))) {
            return "ERROR No Act with ID " + actID + " exists";
        }

        return database.addToPerformanceList(newPerformance); // if there are no more errors then ask database to add performance to performanceList
    }

    public String issueTicket(String performanceID, int rowNumber, int seatNumber) {
        if (performanceID == null || performanceID.equals("")) { // nullcheck on performanceID
            return "ERROR Performance ID is empty";
        }

        if (rowNumber <= 0) { // check legibility of rowNumber
            return "ERROR row cannot be negative or 0";
        }

        if (seatNumber <= 0) { // check legibility of seatNumber
            return "ERROR Seat number cannot be negative or 0";
        }

        Performance actualPerformance = database.findPerformance(performanceID); // need to find performance relating to the performance ID
        if (actualPerformance == null) { // check if no performance was found
            return "ERROR No Performance with ID " + performanceID + " exists";
        }

        Theatre t = database.findTheatre(actualPerformance.getTheatreID()); // find theatre relating to performance ID
        if (rowNumber > t.getSeatingDimension())  { //check if row and seat are out of bounds
            return "ERROR Row Number greater than seating dimension";
        } else if (seatNumber > t.getSeatingDimension()) {
            return "ERROR Seat Number greater than seating dimension";
        }

        Ticket newTicket = new Ticket(performanceID, rowNumber, seatNumber);
        return database.addToTicketList(newTicket); // If all the checks go through then ask database to add ticket to the ticketList
    }

    public List<String> seatsAvailable(String performanceID) {
        ArrayList<String> seatsAvailableList = new ArrayList<>();

        if (performanceID == null || performanceID.equals("")) { // check if performanceID is empty or null
            seatsAvailableList.add("ERROR Performance ID is empty");
            return seatsAvailableList;
        }

        Performance actualPerformance = database.findPerformance(performanceID); //find performance relating to its performanceID
        if (actualPerformance == null) {  // check if there were no performance found
            seatsAvailableList.add("ERROR No Performance with ID " + performanceID + " exists");
            return seatsAvailableList;
        }

        Theatre actualTheatre = database.findTheatre(actualPerformance.getTheatreID()); // find theatre relating to its theatreID
        if (actualTheatre == null) { // check if there were no theatre found
            seatsAvailableList.add("ERROR No theatre relating to the performance" + performanceID + " exists");
            return seatsAvailableList;
        }
        return actualTheatre.getAvailableSeats(database.getTicketList(), seatsAvailableList, performanceID); // request theatre object to find available seats relating to its theatre
    }

    public List<String> salesReport(String actID) {
        ArrayList<String> salesReport = new ArrayList<>();

        if (actID == null || actID.equals("")) {  // check if actID is empty or null
            salesReport.add("ERROR Act ID is empty");
            return salesReport;
        }

        ArrayList<Performance> performanceRelatedToAct = database.findPerformancesRelatingToAct(actID); // Requests the database to find a list of performances related to the actID
        if (performanceRelatedToAct.size() == 0) { // If no performances were found then return
            salesReport.add("ERROR No performances associated with this act");
            return salesReport;
        }

        // We go through each performance relating to the act and we find the theatre relating to that performance and
        // calculate the price of each ticket respectively and finds total seats

        for (Performance p : performanceRelatedToAct) {
            int pPrice = Integer.parseInt(p.getPremiumPrice());
            int cPrice = Integer.parseInt(p.getCheapPrice());
            int totalSeats = 0;
            int totalMoney = 0;
            Theatre theatre = database.findTheatre(p.getTheatreID());

            for (Ticket t : database.getTicketList()) {
                if (t.getPerformanceID().equals(p.getPerformanceID())) {
                    totalMoney += (t.getRowNumber() <= (theatre.getSeatingDimension())/2) ? pPrice : cPrice; // if theatreRowNumber is less than or equal to half the theatre dimension then premiumPrice increment else cheapPrice increment
                    totalSeats++;
                }
            }

            salesReport.add(p.getPerformanceID() + "\t" + p.getStartTime() + "\t" + totalSeats + "\t" + "$" + totalMoney);
        }

        return salesReport;
    }

    public List<String> dump() {
        return null;
    }

}


