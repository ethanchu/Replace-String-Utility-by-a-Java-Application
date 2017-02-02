package edu.gatech.seclass.tourneycalc;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TourneyCalcActivity extends AppCompatActivity {


    private EditText entranceFee;
    private EditText entrantsNumber;
    private EditText housePercentage;
    private EditText houseCutValue;
    private EditText firstPrizeValue;
    private EditText secondPrizeValue;
    private EditText thirdPrizeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourney_calc);

        entranceFee = (EditText) findViewById(R.id.entranceFee);
        entrantsNumber = (EditText) findViewById(R.id.entrantsNumber);
        housePercentage = (EditText) findViewById(R.id.housePercentage);
        houseCutValue = (EditText) findViewById(R.id.houseCutValue);
        firstPrizeValue = (EditText) findViewById(R.id.firstPrizeValue);
        secondPrizeValue = (EditText) findViewById(R.id.secondPrizeValue);
        thirdPrizeValue = (EditText) findViewById(R.id.thirdPrizeValue);

    }

    public void handleClick(View view) {

            houseCutValue.setText("");
            firstPrizeValue.setText("");
            secondPrizeValue.setText("");
            thirdPrizeValue.setText("");

        if (view.getId() == R.id.buttonCalculate) {
            String fee = entranceFee.getText().toString();
            String num = entrantsNumber.getText().toString();
            String precent = housePercentage.getText().toString();
            boolean checkfee = fee.matches("") || Double.parseDouble(fee) <= 0 ;
            boolean checknum = num.matches("") || Double.parseDouble(num) <= 3;
            boolean checkprecent = precent.matches("") || Double.parseDouble(precent) > 100 || Double.parseDouble(precent) < 0;

            if (checkfee) {
                entranceFee.setError("Invalid Fee");
            }

            if (checknum) {
                entrantsNumber.setError("Invalid Number of Entrants");
            }

            if (checkprecent) {
                housePercentage.setError("Invalid House Percentage");
            }

            if ( (!checkfee) && (!checknum)  && (!checkprecent) ) {
                houseCutValue.setText(housecut(Double.parseDouble(fee), Double.parseDouble(num), Double.parseDouble(precent)));
                firstPrizeValue.setText(firstprice(Double.parseDouble(fee), Double.parseDouble(num), Double.parseDouble(precent)));
                secondPrizeValue.setText(secondprice(Double.parseDouble(fee), Double.parseDouble(num), Double.parseDouble(precent)));
                thirdPrizeValue.setText(thirdprice(Double.parseDouble(fee), Double.parseDouble(num), Double.parseDouble(precent)));
            }
        }




    }

    public String firstprice(double entrancefee, double entrants, double houseprecent){
        long  price1st = (long) Math.round(entrancefee * entrants * (1.0-houseprecent/100.0)*0.5);
        return String.valueOf(price1st);
    }

    public String secondprice(double entrancefee, double entrants, double houseprecent){
        long price2nd = (long) Math.round(entrancefee * entrants * (1.0-houseprecent/100.0)*0.3);
        return String.valueOf(price2nd);
    }

    public String thirdprice(double entrancefee, double entrants, double houseprecent){
        long price3rd = (long) Math.round(entrancefee * entrants * (1.0-houseprecent/100.0)*0.2);
        return String.valueOf(price3rd);
    }

    public String housecut(double entrancefee, double entrants, double houseprecent){
        long cuthouse = (long) Math.round(entrancefee * entrants * (houseprecent/100.0));
        return String.valueOf(cuthouse);
    }
}
