package teamspartnz.greenwhale.Adapter;

/**
 * Created by Pz on 10-May-17.
 */

public enum BinderViewType implements jp.satorufujiwara.binder.ViewType {
    USER_CLAIMED,
    REWARDS
    ;

    @Override
    public int viewType() {
        return ordinal();
    }
}
