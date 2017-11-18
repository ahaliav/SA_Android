package com.fox.ahaliav.saapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


public class ContactDetailsFragment extends Fragment {

    private DatePicker dpResult;
    Button btnSave;
    Button btnCancel;
    EditText txtName;
    EditText txtComments;
    EditText txtPhone;
    EditText txtEmail;
    TextView tvTitle;
    View rootView = null;

    Integer id = -1;
    String name = "";
    String phone = "";
    String comments = "";
    String email = "";
    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);

        txtName = (EditText)rootView.findViewById(R.id.txtName);
        txtComments = (EditText)rootView.findViewById(R.id.txtComments);
        txtPhone = (EditText)rootView.findViewById(R.id.txtPhone);
        tvTitle =  (TextView)rootView.findViewById(R.id.tvTitle);
        txtEmail =  (EditText)rootView.findViewById(R.id.txtEmail);

        if(getArguments() != null && getArguments().containsKey("id")){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.contact_edit_title);
            tvTitle.setText(R.string.contact_edit_title);
            id = getArguments().getInt("id");
            name = getArguments().getString("name");
            phone = getArguments().getString("phone");
            comments = getArguments().getString("comments");
            email = getArguments().getString("email");

            txtName.setText(name);
            txtComments.setText(comments);
            txtPhone.setText(phone);
            txtEmail.setText(email);
        }
        else{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.contact_add_title);
            tvTitle.setText(R.string.contact_add_title);
        }

        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
                getFragmentManager().popBackStack();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

    public void save() {

        dpResult = (DatePicker) rootView.findViewById(R.id.dpResult);

        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());

        name = txtName.getText().toString();
        comments = txtComments.getText().toString();
        phone = txtPhone.getText().toString();
        email = txtEmail.getText().toString();

        if(id <=0){
            db.insertContact(name, phone, comments, email);
        }
        else {
            db.updateContact(id, name, phone, comments, email);
        }
    }

}
