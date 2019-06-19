package in.geekofia.blog.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import in.geekofia.blog.R;

public class ContactFragment extends Fragment {

    private TextInputEditText mContactSubject, mContactBody;
    private Button mSendMailButton;
    Spinner mContactSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        getActivity().setTitle(R.string.title_fragment_contact);

        // Contact Spinner
        mContactSpinner = view.findViewById(R.id.contact_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.contact_support_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mContactSpinner.setAdapter(adapter);

        // Views Initialization
        mContactSubject = view.findViewById(R.id.contact_subject);
        mContactBody = view.findViewById(R.id.contact_body);
        mSendMailButton = view.findViewById(R.id.contact_send_button);

        mSendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        return view;
    }

    private void sendMail() {
        String recipient = mContactSpinner.getSelectedItem().toString();
        String subject = mContactSubject.getText().toString();
        String body = mContactBody.getText().toString();


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "There is no Email client installed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
