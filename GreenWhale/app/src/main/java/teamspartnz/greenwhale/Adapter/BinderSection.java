package teamspartnz.greenwhale.Adapter;

import jp.satorufujiwara.binder.Section;

/**
 * Created by Pz on 10-May-17.
 */
public enum BinderSection implements Section {

    SECTION_1;

    @Override
    public int position() {
        return ordinal();
    }
}