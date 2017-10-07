package teamspartnz.greenwhale.Container;

/**
 * Created by P on 10/7/2017.
 */

public class TreeContainer {
    double lat, lon;
    String name;
    String uid;

    public TreeContainer(double lat, double lon, String name, String uid) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.uid = uid;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
