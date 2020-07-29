package com.johanespino.familyconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.EventRegistrationZombieListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    //Vistas
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nametv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    CollectionReference UsersReference;
    ListenerRegistration seenListener;
    CollectionReference useRefforSeen;
    //Listas y adaptadores
    List<Chat> chatList;
    AdapterChat adapterChat;
    //parametros obtenidos
    String hisUid;
    String myUid;
    String hisImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = findViewById(R.id.toolbarchat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = findViewById(R.id.chat_recyclerview);
        profileIv = findViewById(R.id.profilepic);
        nametv = findViewById(R.id.nameTvchat);
        userStatusTv = findViewById(R.id.userStatusTv);
        messageEt = findViewById(R.id.message_id);
        sendBtn = findViewById(R.id.sendBtn);
        userStatusTv = findViewById(R.id.userStatusTv);
        //Layout(LinearLayout) for Reyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //recyclerview properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        hisUid = intent.getStringExtra("SuUID");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UsersReference = firebaseFirestore.collection("users");
        Query userquery = UsersReference.whereEqualTo("uid", hisUid);
        userquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot mDocument : queryDocumentSnapshots.getDocuments()) {
                    String Firstname = "" + mDocument.getString("firstName");
                    hisImage = "" + mDocument.getString("imagen");
//Revisar el estado de tipeo
                    String onlineStatus = "" + mDocument.getString("onlineStatus");
                    if (onlineStatus.equals("online")) {
                        userStatusTv.setText(onlineStatus);
                    } else {
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(onlineStatus));
                        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
                        userStatusTv.setText("Ultima vez: " + dateTime);
                    }
                    //set data
                    nametv.setText(Firstname);
                    try {
                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_profile).into(profileIv);
                    } catch (Exception ex) {
                        Picasso.get().load(R.drawable.ic_profile).into(profileIv);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    //texto ingresado
                    Toast.makeText(ChatActivity.this, "No se puede enviar el mensaje", Toast.LENGTH_SHORT).show();
                } else {

                    sendMessage(message);
                }
            }
        });
        readMessages();
        seenMessages();
        //check edit text
    }

    private void seenMessages() {
        useRefforSeen = FirebaseFirestore.getInstance().collection("chats");
        seenListener = useRefforSeen.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange mDocument : value.getDocumentChanges()) {
                    if (mDocument.getType() == DocumentChange.Type.ADDED) {
                        Chat chat = mDocument.getDocument().toObject(Chat.class);
                        if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)) {
                            HashMap<String, Object> hashseenMap = new HashMap<>();
                            hashseenMap.put("isSeen", true);
                            mDocument.getDocument().getReference().update(hashseenMap);
                        }
                    }
                }
            }
        });

    }

    private void readMessages() {
//completar con lo otro :)
        chatList = new ArrayList<>();
        CollectionReference dbRef = FirebaseFirestore.getInstance().collection("chats");
        dbRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange mDocument : value.getDocumentChanges()) {

                    if (mDocument.getType() == DocumentChange.Type.ADDED) {

                        Chat chat = mDocument.getDocument().toObject(Chat.class);
                        if (chat != null){
                        if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) || chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                            chatList.add(chat);
                        }}
                        adapterChat = new AdapterChat(ChatActivity.this, chatList, hisImage);
                        adapterChat.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterChat);
                    }
                }
            }
        });

    }

    private void sendMessage(String message) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("chats");
        String timeStamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timeStamp", timeStamp);
        hashMap.put("isSeen", false);
        collectionReference.document(timeStamp).set(hashMap);
        messageEt.setText("");


    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
            myUid = user.getUid(); //mi Uid
        } else {
            //user is signed in stay here
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_post).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            onBackPressed();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        //online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //timeStamp
        String timeStamp = String.valueOf(System.currentTimeMillis());
        //offline
        checkOnlineStatus(timeStamp);
        seenListener.remove();
    }

    @Override
    protected void onResume() {
        //online
        checkOnlineStatus("online");
        super.onResume();
    }

    private void checkOnlineStatus(String status) {
        DocumentReference dbref = FirebaseFirestore.getInstance().collection("usuarios").document(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        dbref.update(hashMap);
    }
}