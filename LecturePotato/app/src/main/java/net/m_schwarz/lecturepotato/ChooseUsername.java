package net.m_schwarz.lecturepotato;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import net.m_schwarz.lecturepotato.Network.*;
import android.widget.Toast;

import java.util.UUID;

public class ChooseUsername extends AppCompatActivity {
    int uni;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_username);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uni = extras.getInt("uni");
        }

        ImageButton ib = (ImageButton) findViewById(R.id.imageBox);

        switch(uni){
            case 0:
                ib.setImageResource(R.drawable.uni_ljubljana);
                break;
            case 1:
                ib.setImageResource(R.drawable.tum);
                break;
            case 2:
                ib.setImageResource(R.drawable.ukoper);
                break;
        }
    }

    void clickContinue(View v){
        EditText et = (EditText) findViewById(R.id.username);
        String username = et.getText().toString();

        if(Users.existsUser(username)){
            Toast.makeText(ChooseUsername.this, "Nope", Toast.LENGTH_SHORT).show();
        }
        else {
            Users.createUser(getDeviceId(),username,uni);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
         //   finish();
        }
    }

    public String getDeviceId(){
        SharedPreferences settings = getSharedPreferences("LecturePotato", 0);
        if(!settings.getBoolean("started",false)){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("started",true);

            UUID id = UUID.randomUUID();
            editor.putString("deviceId",id.toString());
            editor.commit();
        }

        return settings.getString("deviceId","");
    }
}
