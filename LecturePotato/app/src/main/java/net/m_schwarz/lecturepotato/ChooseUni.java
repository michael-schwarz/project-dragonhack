package net.m_schwarz.lecturepotato;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseUni extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_uni);
    }

    protected void uniChosen(View v){

        int uni = -1
        switch(v.getId()){
            case R.id.uni1_button:
                uni = 0;
                break;
            case R.id.uni2_button:
                uni = 1;
                break;
            case R.id.uni3_button:
                uni = 2;
                break;
            default:
                uni = -1;
        }
    }
}
