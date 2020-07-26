package com.johanespino.familyconnect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johanespino.familyconnect.R;

public class PerfilUsuario extends Fragment {



    public PerfilUsuario() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        return v;
    }



}