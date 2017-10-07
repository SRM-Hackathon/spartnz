package teamspartnz.greenwhale.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter;
import teamspartnz.greenwhale.Adapter.BinderSection;
import teamspartnz.greenwhale.Adapter.BinderViewType;
import teamspartnz.greenwhale.Adapter.ClaimedBinder;
import teamspartnz.greenwhale.Container.UserClaimedContainer;
import teamspartnz.greenwhale.Login;
import teamspartnz.greenwhale.MainActivity;
import teamspartnz.greenwhale.R;

/**
 * Created by P on 10/7/2017.
 */

public class FragClaimed extends Fragment {
    Context c;

    RecyclerBinderAdapter<BinderSection, BinderViewType> adapter = new RecyclerBinderAdapter<>();
    RecyclerView recyclerView;
    GridLayoutManager ll;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_claimed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        ll = new GridLayoutManager(c, 1);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);

        JsonObject ob = new JsonObject();
        ob.addProperty("email", c.getSharedPreferences("Creden", 0).getString("user", "error"));
        Ion.with(c)
                .load(getString(R.string.claimed))
                .setJsonObjectBody(ob)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result == null)
                            Toast.makeText(c, "Connection error!", Toast.LENGTH_SHORT).show();
                        else {
                            for(int i = 0;i < result.size();i++)    {
                                JsonObject o = result.get(i).getAsJsonObject();
                                double lat = o.get("lat").getAsDouble();
                                double lng = o.get("lng").getAsDouble();
                                String uid = o.get("uid").getAsString();
                                String name = o.get("name").getAsString();

                                UserClaimedContainer userClaimedContainer = new UserClaimedContainer(uid, name, lat, lng);
                                adapter.add(BinderSection.SECTION_1, new ClaimedBinder(getActivity(), userClaimedContainer));

                            }
                        }
                    }

                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        c = context;
    }
}
