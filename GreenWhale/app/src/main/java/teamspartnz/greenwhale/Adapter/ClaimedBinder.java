package teamspartnz.greenwhale.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import jp.satorufujiwara.binder.recycler.RecyclerBinder;
import teamspartnz.greenwhale.Container.UserClaimedContainer;
import teamspartnz.greenwhale.MainActivity;
import teamspartnz.greenwhale.MapsActivity;
import teamspartnz.greenwhale.R;


/**
 * Created by Pz on 10-May-17.
 */

public class ClaimedBinder extends RecyclerBinder<BinderViewType> {
    Activity a;
    UserClaimedContainer userClaimedContainer;
    public ClaimedBinder(Activity activity, UserClaimedContainer container) {
        super(activity, BinderViewType.USER_CLAIMED);
        a = activity;
        userClaimedContainer = container;

    }

    @Override
    public int layoutResId() {
        return R.layout.card_claimed;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v) {
        return new Holder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Holder holder = (Holder) viewHolder;
        holder.name.setText(userClaimedContainer.getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(a, MapsActivity.class);
                i.putExtra("lat",userClaimedContainer.getLat());
                i.putExtra("lng",userClaimedContainer.getLng());
                i.putExtra("name",userClaimedContainer.getName());
                i.putExtra("uid",userClaimedContainer.getUid());
                a.startActivity(i);
            }
        });
    }


    private static class Holder extends RecyclerView.ViewHolder {
        TextView name;
        CardView card;
        private Holder(View view) {
            super(view);
            card = (CardView)view.findViewById(R.id.card);
            name = (TextView)view.findViewById(R.id.name);

        }
    }
}
