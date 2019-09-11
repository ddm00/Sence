package com.turkcell.sence.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRequestAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<User> myList;
    private FragmentManager supportFragmentManager;

    public UserRequestAdapter(Context context, List<User> myList, FragmentManager supportFragmentManager) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myList = myList;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    public List<User> getMyList() {
        return myList;
    }

    public void updateResults(List<User> userList) {
        this.myList = userList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.list_view_item_user_request, null);

        CircleImageView image_profile = view.findViewById(R.id.userrequestUserImage);
        TextView username = view.findViewById(R.id.userrequestUsername);
        TextView fullname = view.findViewById(R.id.userrequestFullname);
        ImageView no = view.findViewById(R.id.userrequestNo);
        ImageView yes = view.findViewById(R.id.userrequestYes);

        final User user = myList.get(position);

        username.setText(user.getUsername());
        fullname.setText(user.getFullname());
        Glide.with(context).load(user.getImageurl()).into(image_profile);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                        .child("followers").child(user.getId()).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                        .child("following").child(MainActivity.CurrentUser.getId()).setValue(true);

                FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                        .child("requestGet").child(user.getId()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                        .child("requestPust").child(MainActivity.CurrentUser.getId()).removeValue();

                myList.remove(position);
                updateResults(myList);

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                        .child("requestGet").child(user.getId()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                        .child("requestPust").child(MainActivity.CurrentUser.getId()).removeValue();
                myList.remove(position);
                updateResults(myList);
            }
        });

        return view;
    }
}
