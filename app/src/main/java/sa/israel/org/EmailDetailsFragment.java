package sa.israel.org;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import sa.israel.org.R;


public class EmailDetailsFragment extends Fragment {

    EditText txtName;
    EditText txtPhone;
    EditText txtEmail;
    EditText txtComments;
    Button btnSendMessage;
    Button btnCancel;

    public EmailDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_email_details, container, false);

        txtName = (EditText)v.findViewById(R.id.txtName);
        txtPhone = (EditText)v.findViewById(R.id.txtPhone);
        txtEmail = (EditText)v.findViewById(R.id.txtEmail);
        txtComments = (EditText)v.findViewById(R.id.txtComments);

        btnSendMessage = (Button)v.findViewById(R.id.btnSendMessage);
        btnCancel = (Button)v.findViewById(R.id.btnCancel);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendmessage();
                getFragmentManager().popBackStack();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private void sendmessage() {

    }
}
