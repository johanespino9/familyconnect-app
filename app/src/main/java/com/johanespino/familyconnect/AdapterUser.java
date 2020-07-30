package com.johanespino.familyconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder> {
    Context context;
    List<ModelUser> userList;

    public AdapterUser(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String hisUID = userList.get(i).getUid();
        String userImage = userList.get(i).getImagen();
        String userName = userList.get(i).getFirstName();
        String userLastName = userList.get(i).getLastName();
        final String userEmail = userList.get(i).getEmail();
        //colocar data
        String userCompleteName=userName+" "+userLastName;
        myHolder.nNameIv.setText(userCompleteName);
        myHolder.nEmailIv.setText(userEmail);
        try {
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_profile_default)
                    .into(myHolder.mimagenIv);
        } catch (Exception ex) {

        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //visualizar dialogo
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(new String[]{"Perfil", "Chat"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //para el perfil
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("SuUID", hisUID);
                            context.startActivity(intent);
                        }
                        if (which == 1) {
                            //para el chat
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("SuUID", hisUID);
                            context.startActivity(intent);
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView mimagenIv;
        TextView nNameIv, nEmailIv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mimagenIv = itemView.findViewById(R.id.profileIv);
            nNameIv = itemView.findViewById(R.id.nametv);
            nEmailIv = itemView.findViewById(R.id.emailtv);
        }
    }
}
