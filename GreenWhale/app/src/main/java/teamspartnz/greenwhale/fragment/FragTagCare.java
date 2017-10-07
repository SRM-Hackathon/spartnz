package teamspartnz.greenwhale.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import teamspartnz.greenwhale.CareTree;
import teamspartnz.greenwhale.R;
import teamspartnz.greenwhale.TagTree;

/**
 * Created by P on 10/7/2017.
 */

public class FragTagCare extends Fragment {
    Button tag, care;
    Context c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tag = (Button) view.findViewById(R.id.tag);
        care = (Button) view.findViewById(R.id.care);

        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(c, TagTree.class));

            }
        });

        care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(c, CareTree.class));
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        c = context;
    }
}
