package teamspartnz.greenwhale.Container;

/**
 * Created by P on 10/7/2017.
 */

public class RewardContainer {
    String offer, karma;

    public RewardContainer(String offer, String karma) {
        this.offer = offer;
        this.karma = karma;
    }

    public String getOffer() {
        return offer;
    }

    public String getKarma() {
        return karma;
    }
}
