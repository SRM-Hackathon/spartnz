package teamspartnz.greenwhale.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import teamspartnz.greenwhale.Adapter.REwardBinder;
import teamspartnz.greenwhale.Container.RewardContainer;
import teamspartnz.greenwhale.R;

/**
 * Created by P on 10/7/2017.
 */

public class Rewards extends Fragment {
    Context c;
    ProgressDialog progressDialog;

    RecyclerBinderAdapter<BinderSection, BinderViewType> adapter = new RecyclerBinderAdapter<>();
    RecyclerView recyclerView;
    GridLayoutManager ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_rewards, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        ll = new GridLayoutManager(c, 1);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject ob = new JsonObject();
        ob.addProperty("xxx", "xxx");
        Ion.with(Rewards.this)
                .load(getResources().getString(R.string.rewards))
                .setJsonObjectBody(ob)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        progressDialog.cancel();
                        if (result == null)
                            Toast.makeText(c, "Connection error!", Toast.LENGTH_SHORT).show();
                        else {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject o = result.get(i).getAsJsonObject();
                                String reward = o.get("offer").getAsString();
                                String karma = o.get("karma").getAsString();
                                RewardContainer container = new RewardContainer(reward, karma);
                                adapter.add(BinderSection.SECTION_1, new REwardBinder(getActivity(), container));
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
