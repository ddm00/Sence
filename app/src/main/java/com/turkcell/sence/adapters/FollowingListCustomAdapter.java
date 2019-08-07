package com.turkcell.sence.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.turkcell.sence.R;
import com.turkcell.sence.models.User;
import com.turkcell.sence.models.UserProfileSurvey;

import java.util.ArrayList;
import java.util.List;

public class FollowingListCustomAdapter extends BaseAdapter {


    private Context mContext;
    private List<User> mUsers;

    public FollowingListCustomAdapter(Context context, ArrayList<User> mUsers){
        this.mContext = context;
        this.mUsers = mUsers;
    }
    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(mContext, R.layout.user_list_item, null);

        TextView isimsoyisim = view.findViewById(R.id.user_list_item_tv);
        ImageView image1 = view.findViewById(R.id.user_list_item_iv);

        image1.setBackgroundResource(mUsers.get(position).getProfilePhoto());
        isimsoyisim.setText(mUsers.get(position).getFullname());


        return view;
    }
}
