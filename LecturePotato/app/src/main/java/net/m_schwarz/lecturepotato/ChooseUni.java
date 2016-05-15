package net.m_schwarz.lecturepotato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseUni extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_uni);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    protected void uniChosen(View v){

        int uni = -1;
        switch(v.getId()){
            case R.id.uni1_button:
                uni = 1;
                break;
            case R.id.uni2_button:
                uni = 2;
                break;
            case R.id.uni3_button:
                uni = 3;
                break;
            default:
                uni = -1;
        }

        Intent intent = new Intent(this,ChooseUsername.class);
        intent.putExtra("uni",uni);
        startActivity(intent);
    }
}
