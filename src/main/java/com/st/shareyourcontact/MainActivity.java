package com.st.shareyourcontact;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.Activity;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.telephony.SmsManager;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;

public class MainActivity extends Activity {

    Button buttonSend;
    EditText textPhoneNo;
    EditText textSMS;
    ListView listView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        listView = (ListView) findViewById(R.id.contactsView);



        Cursor c = this.managedQuery(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.Data.MIMETYPE + "=?", // condition
                new String[] { ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE }, // value
                null);

        final ArrayList<Contact> contacts = new ArrayList<Contact>();

        while (c.moveToNext()) {
            int type = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                Contact con = new Contact(c.getString(c
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), c.getString(c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contacts.add(con);
            }
        }
        listView.setAdapter(new ContactsAdapter(this, contacts));



        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listView = (ListView) findViewById(R.id.contactsView);
                String phoneNo = textPhoneNo.getText().toString();
                //String sms = textSMS.getText().toString();
                int i = 0;
                int virus=0;


                for (int j=0; j<listView.getCount();j++){
                    View item = (View) listView.getChildAt(j);
                    boolean selected = ((CheckBox) item.findViewById(R.id.selected)).isChecked();
                    CheckBox chk = (CheckBox) item.findViewById(R.id.selected);
                    chk.setChecked(false);
                    if (selected) {i=j; virus++; }
                }


                //View item = (View) listView.getChildAt(0);
                //int i=0;
                View item = (View) listView.getChildAt(i);

                String num = ((TextView) item.findViewById(R.id.mobile)).getText().toString();
                String nam = ((TextView) item.findViewById(R.id.name)).getText().toString();
                //Toast.makeText(getApplicationContext(), nam+" "+num+" sent", Toast.LENGTH_SHORT).show();

                String sms = ("You recieved a new contact:\nName: "+nam+"\nNumber: "+num+"\nSent via ShareYourContact App made by Pavel Mularchick");
                //Toast.makeText(getApplicationContext(), sms, Toast.LENGTH_LONG).show();

                if (virus>1) {
                    Toast.makeText(getApplicationContext(), "YOU ONLY ALLOWED TO SEND ONE CONTACT PER TRY", Toast.LENGTH_LONG).show();
                } else {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "Contact "+nam+" sent to number "+phoneNo,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }} //closing for cycle

            }
        });
    }
}