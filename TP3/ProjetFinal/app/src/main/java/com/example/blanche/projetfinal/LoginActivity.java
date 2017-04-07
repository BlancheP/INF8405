package com.example.blanche.projetfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Fragment {

    private static String currentUser = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflating the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_login, null);

        final EditText etUsername = (EditText) view.findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        final Button bLogin = (Button) view.findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) view.findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegisterActivity());
                fragmentTransaction.commit();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.userIsValid(etUsername.getText().toString(), etPassword.getText().toString(), getActivity());
            }
        });
        return view;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String user) {
        currentUser = user;
    }
}
