package teamspartnz.greenwhale.Container;

/**
 * Created by P on 10/7/2017.
 */

public class UserClaimedContainer {
    String uid, name;
    double lat, lng;

    public UserClaimedContainer(String uid, String name, double lat, double lng) {
        this.uid = uid;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

}
