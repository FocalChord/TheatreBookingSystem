package tbs.server;

import java.util.ArrayList;

/*
 * Created by Nisarag on 7/04/2018.
 * The Act class, it is an integral part of the TheatreBookingSystem since every performance consists of an act so
 * the two objects are related and communicate with each other quite heavily
 * param: actID - this is a String that contains the ID of the act
 * param: title - this is an String which indicates the title of the act
 * param: minutesDuration - this is an integer which indicates how long the act is going to be
 *
 * Contains the usual getMethods, it also contains a method to create a unique actID from the field actIDCounter, and
 * also some error checking methods which are used when initializing an act object
 */


public class Act {
        private String actID;
        private String title;
        private String artistID;
        private int minutesDuration;
        private static long actIDCounter = 0;

        public Act(String title, String artistID, int minutesDuration){
            this.actID = createActID();
            this.artistID = artistID;
            this.title = title;
            this.minutesDuration = minutesDuration;
        }

        public String getActID(){
            return actID;
        }

        public String getArtistID(){
            return artistID;
        }

        public String createActID() {
            return "AC" + String.valueOf(actIDCounter++);
        }

        public boolean nullTitleCheck(String title) {
            return (title == null || title.equals(""));
        }

        public boolean nullArtistIDCheck(String artistID) {
            return (artistID == null || artistID.equals(""));
        }

        public boolean minutesCheck(int minutesDuration) {
            return (minutesDuration <= 0);
        }

        public boolean artistIDExists(ArrayList<Artist> artistList, String artistID) {
            for (Artist a : artistList) {
                if (a.getArtistId().equals(artistID)) {
                    return true;
                }
            }
            return false;
        }
}
