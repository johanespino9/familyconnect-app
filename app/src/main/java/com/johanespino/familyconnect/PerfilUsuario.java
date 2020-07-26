package com.johanespino.familyconnect;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
public class PerfilUsuario extends Fragment {
    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    //Storage
    private StorageReference storageReference;
    String storagePath = "Users_Profile_Imgs/";
    ImageView avatarTv;
    TextView lastName_User, firstName_User, email_User;
    FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;
    //permisos contants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;


    String cameraPermissions[];
    String storagePermissions[];
    Uri image_uri;
    String uid;
    String profileOrCoverPhoto;
    public PerfilUsuario() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();//firebase storage reference
        //remover la idea de los distritos cambiar los datos anteriores alinearlo al del video, usar solo la estructura del video.
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //iniciar vistas
        avatarTv = view.findViewById(R.id.avatarIv);
        firstName_User = view.findViewById(R.id.firstName_User);
        lastName_User = view.findViewById(R.id.lastName_User);
        email_User = view.findViewById(R.id.email_User);
        floatingActionButton = view.findViewById(R.id.fab);
        progressDialog = new ProgressDialog(getActivity());

        user = firebaseAuth.getCurrentUser();
        uid=user.getUid();
        checkUserStatus();
        getUserProfile(uid);
        return view;

    }

    public void getUserProfile(String uid){

        firebaseFirestore.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String firstName=documentSnapshot.getString("firstName");
                    String lastName=documentSnapshot.getString("lastName");
                    String email=documentSnapshot.getString("email");
                    String image=documentSnapshot.getString("imagen");
                    firstName_User.setText(firstName);
                    lastName_User.setText(lastName);
                    email_User.setText(email);
                    try {
                        Picasso.get().load(image).into(avatarTv);
                    } catch (Exception ex) {
                        Picasso.get().load(R.drawable.ic_profile_default).into(avatarTv);
                    }



                }
            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        } else {
            //user is signed in stay here
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

}