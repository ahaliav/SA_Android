package sa.israel.org;

/**
 * Created by ahaliav_fox on 30 אוקטובר 2017.
 */

public class Group {
    public Group (String day, String fromTime, String toTime, String comment, String location, String lang, float latitude,float longitude, float km, int dayNum) {
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.comment = comment;
        this.location = location;
        this.lang = lang;
        this.latitude = latitude;
        this.longitude = longitude;
        this.km = km;
        this.dayNum = dayNum;
    }

    private String day = "";
    public String getDay(){
        return day;
    }

    private String fromTime = "";
    public String getFromTime(){
        return fromTime;
    }

    private String toTime = "";
    public String getToTime(){
        return toTime;
    }

    private String comment = "";
    public String getComment(){
        return comment;
    }

    private String location = "";
    public String getLocation(){
        return location;
    }

    private String lang = "";
    public String getLang(){
        return lang;
    }

    private float latitude = 0;
    public float getLatitude(){
        return latitude;
    }

    private float longitude = 0;
    public float getLongitude(){
        return longitude;
    }

    private int dayNum = 0;
    public int getDayNum(){
        return dayNum;
    }

    private float km = 0;
    public float getKm(){
        return km;
    }


}
