package teamspartnz.greenwhale.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import jp.satorufujiwara.binder.recycler.RecyclerBinder;
import teamspartnz.greenwhale.Container.RewardContainer;
import teamspartnz.greenwhale.R;


/**
 * Created by Pz on 10-May-17.
 */

public class REwardBinder extends RecyclerBinder<BinderViewType> {
    Activity a;
    RewardContainer container;
    ProgressDialog progressDialog;

    public REwardBinder(Activity activity, RewardContainer rewardContainer) {
        super(activity, BinderViewType.USER_CLAIMED);
        a = activity;
        container = rewardContainer;

    }

    @Override
    public int layoutResId() {
        return R.layout.card_rewards;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v) {
        return new Holder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Holder holder = (Holder) viewHolder;
        holder.offer.setText(container.getOffer());
        holder.karma.setText(container.getKarma());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(a);
                progressDialog.setMessage("loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                JsonObject ob = new JsonObject();
                ob.addProperty("karma", container.getKarma());
                ob.addProperty("email", a.getSharedPreferences("Creden", 0).getString("user", "error"));
                Ion.with(a)
                        .load(a.getString(R.string.redeem))
                        .setJsonObjectBody(ob)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                progressDialog.cancel();
                                if (result == null)
                                    Toast.makeText(a, "Connection error!", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(a, result.get("details").getAsString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    private static class Holder extends RecyclerView.ViewHolder {
        TextView offer, karma;
        CardView card;

        private Holder(View view) {
            super(view);
            offer = (TextView) view.findViewById(R.id.offer);
            karma = (TextView) view.findViewById(R.id.karma);
            card = (CardView) view.findViewById(R.id.card);

        }
    }
}
