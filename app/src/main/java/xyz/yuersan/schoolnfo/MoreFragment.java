package xyz.yuersan.schoolnfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MoreFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CardView theme;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);
        initView();
        return  view;
    }

    private void initView() {
        theme = (CardView) view.findViewById(R.id.theme);
        theme.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == theme){
            Intent intent = new Intent(getActivity(), ThemeActivity.class);
            startActivity(intent);
        }
    }
}
