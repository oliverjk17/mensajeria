package com.optic.whatsappclone2.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.optic.whatsappclone2.R;
import com.optic.whatsappclone2.activities.ChatActivity;
import com.optic.whatsappclone2.activities.StatusDetailActivity;
import com.optic.whatsappclone2.models.Chat;
import com.optic.whatsappclone2.models.Message;
import com.optic.whatsappclone2.models.Status;
import com.optic.whatsappclone2.models.User;
import com.optic.whatsappclone2.providers.AuthProvider;
import com.optic.whatsappclone2.providers.MessagesProvider;
import com.optic.whatsappclone2.providers.StatusViewerProvider;
import com.optic.whatsappclone2.providers.UsersProvider;
import com.optic.whatsappclone2.utils.RelativeTime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    FragmentActivity context;
    AuthProvider authProvider;
    UsersProvider usersProvider;
    MessagesProvider messagesProvider;
    StatusViewerProvider statusViewerProvider;
    User user;

    ArrayList<Status> statusList;

    Gson gson = new Gson();

    public StatusAdapter(FragmentActivity context, ArrayList<Status> statusList) {
        this.context = context;
        this.statusList = statusList;
        authProvider = new AuthProvider();
        usersProvider = new UsersProvider();
        messagesProvider = new MessagesProvider();
        statusViewerProvider = new StatusViewerProvider();
        user = new User();
    }


    private void getUserInfo(final ViewHolder holder, String idUser) {

        usersProvider.getUserInfo(idUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(User.class);
                        holder.textViewUsername.setText(user.getUsername());
                    }
                }

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Status[] statusGSON = gson.fromJson(statusList.get(position).getJson(), Status[].class);
        holder.circularStatusView.setPortionsCount(statusGSON.length);

        setPortionsColor(statusGSON, holder, position);
        setImageStatus(statusGSON, holder);
        getUserInfo(holder, statusList.get(position).getIdUser());

        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StatusDetailActivity.class);
                intent.putExtra("status", statusList.get(position).getJson());

                // significa que el usuario ya observo todos los estados
                if ((statusList.get(position).getCounter() + 1) == statusGSON.length) {
                    intent.putExtra("counter", 0);
                }
                else {
                    intent.putExtra("counter", statusList.get(position).getCounter() + 1);
                }

                context.startActivity(intent);
            }
        });
    }

    private void setPortionsColor(Status[] statusGSON, final ViewHolder holder, final int position) {

        for (int i = 0; i < statusGSON.length; i++) {
            final int finalI = i;
            statusViewerProvider.getStoryViewerById(authProvider.getId() + statusGSON[i].getId()).addSnapshotListener(context, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null) {
                        // saber si el usuario ya miro un estado
                        if (documentSnapshot.exists()) {
                            holder.circularStatusView.setPortionColorForIndex(finalI, context.getResources().getColor(R.color.colorGrayStatus));
                            statusList.get(position).setCounter(finalI);
                        }
                        else {
                            holder.circularStatusView.setPortionColorForIndex(finalI, context.getResources().getColor(R.color.colorGreenStatus));
                        }

                    }
                    else {
                        holder.circularStatusView.setPortionColorForIndex(finalI, context.getResources().getColor(R.color.colorGreenStatus));
                        statusList.get(position).setCounter(0);
                    }
                }
            });

        }

    }

    private void setImageStatus(Status[] statusGSON, ViewHolder holder) {
        if (statusGSON.length > 0) {
            Picasso.with(context).load(statusGSON[statusGSON.length - 1].getUrl()).into(holder.circleImageUser);
            String relativeTime = RelativeTime.timeFormatAMPM(statusGSON[statusGSON.length - 1].getTimestamp(), context);
            holder.textViewDate.setText(relativeTime);
        }
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUsername;
        TextView textViewDate;
        CircleImageView circleImageUser;
        CircularStatusView circularStatusView;

        View myView;

       public ViewHolder(View view) {
           super(view);
           myView = view;
           textViewUsername = view.findViewById(R.id.textViewUsername);
           textViewDate = view.findViewById(R.id.textViewDate);
           circleImageUser = view.findViewById(R.id.circleImageUser);
           circularStatusView = view.findViewById(R.id.circularStatusView);
       }

   }
}
