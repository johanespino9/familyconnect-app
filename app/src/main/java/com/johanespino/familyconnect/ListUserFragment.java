package com.johanespino.familyconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListUserFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterUser adapterUser;
    List<ModelUser> userList;
    FirebaseFirestore firestore;
    FirebaseFirestore firestore1;
    FirebaseAuth firebaseAuth;
    List<ModelParticipant> uidList = new ArrayList<>();

    public ListUserFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_user, container, false);
        recyclerView = view.findViewById(R.id.family_recyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore1 = FirebaseFirestore.getInstance();
        //propiedades del recycler view
        userList = new ArrayList<>();
//getAll users
        getAllUsers();
        return view;
    }

    private void getAllUsers() {
        //Buscar el metodo para sharedpreferences en fragment

        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("groupcredenciales", Context.MODE_PRIVATE);
        String groupid = preferences.getString("groupId", "No existe la informacion");
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("groups").document(groupid).
                collection("participants").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange mDocument : value.getDocumentChanges()) {
                    if (mDocument.getType() == DocumentChange.Type.ADDED) {
                        final ModelParticipant modelParticipant = mDocument.getDocument().toObject(ModelParticipant.class);
                        firestore1.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentChange querySnapshot1 : value.getDocumentChanges()) {
                                    if (querySnapshot1.getType() == DocumentChange.Type.ADDED) {
                                        ModelUser User = querySnapshot1.getDocument().toObject(ModelUser.class);
                                        if (modelParticipant.getUid().equals(User.getUid()) && !modelParticipant.getUid().equals(fUser.getUid())) {
                                            userList.add(User);
                                        }
                                        adapterUser = new AdapterUser(getActivity(), userList);
                                        recyclerView.setAdapter(adapterUser);
                                    }
                                }

                            }

                        });

                    }

                }
            }
        });


    }


}