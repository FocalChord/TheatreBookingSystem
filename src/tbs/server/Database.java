package tbs.server;


/*
 * Created by Nisarag on 7/04/2018.
 * The Database class, it contains all the important information that is required for the server to use such as lists
 * No params since for every server we only need one database
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Database {

    // Creating required lists of objects for the database
    private ArrayList<Theatre> theatreList = new ArrayList<>();
    private ArrayList<Artist> artistList = new ArrayList<>();
    private ArrayList<Act> actList = new ArrayList<>();
    private ArrayList<Performance> performanceList = new ArrayList<>();
    private ArrayList<Ticket> ticketList = new ArrayList<>();
    private HashMap<String, int[][]> theatreSeats = new HashMap<>(); // HashMap with performanceID as Key and theatreSeats double array as Value



    public ArrayList<Theatre> getTheatreList() {
        return this.theatreList;
    }

    public ArrayList<Act> getActList() {
        return this.actList;
    }

    public ArrayList<Artist> getArtistList() {
        return this.artistList;
    }

    public ArrayList<Ticket> getTicketList() { return this.ticketList; }

    // setTheatreList is only used in initialize  when we read the csv file and need to store data

    public void setTheatreList(String theatreID , int seatingDimension, int floorArea) {
        this.theatreList.add(new Theatre(theatreID, seatingDimension, floorArea));
    }

    // All query methods returns the list in sorted order after finding the required field for the corresponding object

    public List<String> queryTheatreIDs(ArrayList<String> theatreIDList) {
        for (Theatre t : this.theatreList) {
            theatreIDList.add(t.getTheatreId());
        }

        Collections.sort(theatreIDList); // Sort the list lexicographically
        return theatreIDList;
    }

    public List<String> queryArtistIDs(ArrayList<String> artistIDList) {
        for (Artist a : this.artistList) {
            artistIDList.add(a.getArtistId());
        }

        Collections.sort(artistIDList); // Sort the list lexicographically
        return artistIDList;
    }

    public List<String> queryArtistNames(ArrayList<String> artistNameList) {
        for (Artist a : this.artistList) {
            artistNameList.add(a.getArtistName());
        }

        Collections.sort(artistNameList); // Sort the list lexicographically
        return artistNameList;
    }

    public List<String> queryActIDs(ArrayList<String> actIDsList, String artistID) {
        for (Artist a : this.artistList) {
            if (a.getArtistId().equals(artistID)) {
                for (Act ac : this.actList) {
                    if (ac.getArtistID().equals(artistID)) {
                        actIDsList.add(ac.getActID());
                    }
                }
                Collections.sort(actIDsList); // Sort the list lexicographically
                return actIDsList;
            }
        }

        actIDsList.add("ERROR No Artist with that ID"); // if it goes through and the artistID is not found then return
        return actIDsList;
    }

    public List<String> queryPerformanceIDs(ArrayList<String> performanceIDsList, String actID) {
        for (Act a : this.actList) {
            if (a.getActID().equals(actID)) {
                for (Performance p : this.performanceList) {
                    if (p.getActID().equals(actID)) {
                        performanceIDsList.add(p.getPerformanceID());
                    }
                }
                Collections.sort(performanceIDsList); // Sort the list lexicographically
                return performanceIDsList;
            }
        }

        performanceIDsList.add("ERROR No Act with that ID"); // if it goes through and the performanceID is not found then return
        return performanceIDsList;
    }

    public List<String> queryTicketIds(ArrayList<String> ticketIDsList, String performanceID) {
        for (Performance p : this.performanceList) {
            if (p.getPerformanceID().equals(performanceID)) {
                for (Ticket t : this.ticketList) {
                    if (t.getPerformanceID().equals(performanceID)) {
                        ticketIDsList.add(t.getTicketID());
                    }
                }
                Collections.sort(ticketIDsList); // Sort the list lexicographically
                return ticketIDsList;
            }
        }

        ticketIDsList.add("ERROR No Performance with that ID"); // if it goes through and the performanceID is not found then return
        return ticketIDsList;
    }

    // All addTo methods has an input as the corresponding object and adds that object to its corresponding list.

    public String addToArtistList(Artist newArtist) {
        this.artistList.add(newArtist);
        return newArtist.getArtistId();
    }

    public String addToActList(Act newAct) {
        this.actList.add(newAct);
        return newAct.getActID();
    }

    public String addToPerformanceList(Performance newPerformance) {
        this.performanceList.add(newPerformance);
        Theatre theatre = findTheatre(newPerformance.getTheatreID()); //  finds the theatre relating to the performance
        this.theatreSeats.put(newPerformance.getPerformanceID(), new int[theatre.getSeatingDimension()][theatre.getSeatingDimension()]); // Puts the performanceID and a 2d array of the theatreSeats into the HashMap (later used in addToTicketList)
        return newPerformance.getPerformanceID();
    }

    public String addToTicketList(Ticket newTicket) {

        int[][] theatreArray = this.theatreSeats.get(newTicket.getPerformanceID()); // gets theatreArray which is essentially a boolean double array which contains 0 and 1 (0 if the seat is not taken and 1 if it is)

        // The following double for loop goes through the theatreArray and check if a seat is taken and if it is not,
        // if it is then return an Error and if it isn't then mark it as a 1 and then add the ticket to ticketList

        for (int i = 0; i < theatreArray.length; i++) {
            for (int j = 0; j < theatreArray[i].length; j++) {
                if (i == newTicket.getRowNumber()-1 && j == newTicket.getSeatNumber()-1 && theatreArray[i][j] == 1) {
                    return "ERROR That seat is already reserved";
                } else if (i == newTicket.getRowNumber()-1 && j == newTicket.getSeatNumber()-1 && theatreArray[i][j] != 1) {
                    theatreArray[i][j] = 1;
                }
            }
        }

        this.ticketList.add(newTicket);
        return newTicket.getTicketID();
    }

    // These find methods have an input of an object ID and the methods have to find certain objects to that object ID

    public Performance findPerformance(String performanceID) {
        Performance actualPerformance = null;
        for (Performance p : this.performanceList) {
            if (p.getPerformanceID().equals(performanceID)) {
                actualPerformance = p;
                break;
            }
        }
        return actualPerformance;
    }

    public Theatre findTheatre(String theatreId) {
        Theatre actualTheatre = null;
        for (Theatre t : this.theatreList) {
            if (t.getTheatreId().equals(theatreId)) {
                actualTheatre = t;
                break;
            }
        }
        return actualTheatre;
    }

    public ArrayList<Performance> findPerformancesRelatingToAct(String actID) {
        ArrayList<Performance> performanceRelatedToActList = new ArrayList<>();
        for (Performance p : this.performanceList) {
            if (p.getActID().equals(actID)) {
                performanceRelatedToActList.add(p);
            }
        }
        return performanceRelatedToActList;
    }



}
