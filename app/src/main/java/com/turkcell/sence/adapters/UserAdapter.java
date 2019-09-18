package com.turkcell.sence.adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.fragments.profile.OldProfileFragment;
import com.turkcell.sence.models.User;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder> {

    private Activity activity;
    private List<User> mUsers;
    private FragmentManager supportFragmentManager;


    public UserAdapter(Activity activity, List<User> users, FragmentManager supportFragmentManager) {
        this.activity = activity;
        mUsers = users;
        this.supportFragmentManager = supportFragmentManager;

    }

    @NonNull
    @Override
    public UserAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ImageViewHolder holder, final int position) {

        final User user = mUsers.get(position);

        holder.btn_follow.setVisibility(View.VISIBLE);
        isFollowing(user, holder.btn_follow);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFullname());
        Glide.with(activity).load(user.getImageurl()).into(holder.image_profile);

        if (user != null && user.getId() != null && user.getId().equals(MainActivity.CurrentUser.getId())) {
            holder.btn_follow.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *  Bu bölümü eğer Search kısmındaki profil bölümünün alternatif görünümünü değiştirmek istersek.
                 *
                 FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                 NewProfileFragment ProfileFragment = new NewProfileFragment(supportFragmentManager, user, activity);
                 transaction.replace(R.id.fragmentContainer, ProfileFragment, "OldProfileFragment");
                 transaction.addToBackStack(null);
                 transaction.commit();*/
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                OldProfileFragment ProfileFragment = new OldProfileFragment(supportFragmentManager, user, activity);
                transaction.replace(R.id.fragmentContainer, ProfileFragment, "OldProfileFragment");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.CurrentUser.getId() != null && user != null) {
                    if (holder.btn_follow.getText().toString().equals("takip et")) {
                        if (user.isOpen()) {
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                    .child("following").child(user.getId()).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                    .child("followers").child(MainActivity.CurrentUser.getId()).setValue(true);

                            if (user.getToken() != null && !user.getToken().equals("")) {
                                sendFCMPush(user.getToken(), "Sence", MainActivity.CurrentUser.getFullname() + " takip etti");
                            }

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                    .child("requestPust").child(user.getId()).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                    .child("requestGet").child(MainActivity.CurrentUser.getId()).setValue(true);

                            if (user.getToken() != null && !user.getToken().equals("")) {
                                sendFCMPush(user.getToken(), "Sence", MainActivity.CurrentUser.getFullname() + " bir arkadaşlık isteği gönderdi.");
                            }

                        }

                    } else if (holder.btn_follow.getText().toString().equals("takip etme")) {

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                .child("following").child(user.getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("followers").child(MainActivity.CurrentUser.getId()).removeValue();

                    } else if (holder.btn_follow.getText().toString().equals("istek")) {

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                .child("requestPust").child(user.getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("requestGet").child(MainActivity.CurrentUser.getId()).removeValue();
                    }
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView fullname;
        CircleImageView image_profile;
        Button btn_follow;

        ImageViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.userName_Tv);
            fullname = itemView.findViewById(R.id.fullName_Tv);
            image_profile = itemView.findViewById(R.id.imageProfile_Iv);
            btn_follow = itemView.findViewById(R.id.follow_Btn);
        }
    }

    private void isFollowing(final User user, final Button button) {

        if (user.isOpen()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Follow").child(MainActivity.CurrentUser.getId()).child("following");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (user.getId() != null && !user.getId().isEmpty() && dataSnapshot.child(user.getId()).exists()) {
                        button.setText("takip etme");
                    } else {
                        button.setText("takip et");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                    .child("Follow").child(MainActivity.CurrentUser.getId()).child("requestPust");
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (user.getId() != null && !user.getId().isEmpty() && dataSnapshot.child(user.getId()).exists()) {
                        button.setText("istek");
                    } else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                .child("Follow").child(MainActivity.CurrentUser.getId()).child("following");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (user.getId() != null && !user.getId().isEmpty() && dataSnapshot.child(user.getId()).exists()) {
                                    button.setText("takip etme");
                                } else {
                                    button.setText("takip et");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                button.setText("takip et");
                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendFCMPush(String token, String title, String msg) {
        final String SERVER_KEY = "AAAAf88Hcr8:APA91bEzHQIJna7TyTIdIA9G7QYbkDx-2Wvblseslr8xgsjtgeXnxy_SdsBSBnbCWuIvlKZx1ELDdbhNOJw4RQ13Q0UVWXDrpZB4fOdtjL1rcN5bgoBLk96CepDJKnhePEQOVZQDGYqL";

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("return here>>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("True", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("False", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }
}