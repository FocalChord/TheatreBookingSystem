package tbs.server;

import java.util.ArrayList;

/*
 * Created by Nisarag on 7/04/2018.
 * The Artist class, it is an integral part of the TheatreBookingSystem since every performance consists of an act so
 * and both of them need artists since you can't have a performance or an act without an artist.
 *
 * param: artistID - this is a String that contains the ID of the artist
 * param: artistName - this is an String which indicates the name of the artist
 *
 * Contains the usual getMethods, it also contains a method to create a unique artistID from the field artistID, and
 * also some error checking methods which are used when initializing an artist object
 */

public class Artist {
    private String artistID;
    private String artistName;
    private static long artistIDCounter = 0;

    public Artist(String artistName){
        this.artistID = createArtistID();
        this.artistName = artistName;
    }

    public String getArtistId(){
        return artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public String createArtistID() {
        return "AR" + String.valueOf(artistIDCounter++);
    }

    public boolean nullArtistNameCheck(String name) {
        return (name == null || name.equals(""));
    }

    public boolean duplicateArtistNameCheck(ArrayList<Artist> artistList, String name) {

        // Goes through artistList and checks if there is a duplicate and makes sure that artist does not already exist
        // before adding to artistList
        for (Artist a : artistList) {
            if (a.getArtistName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
