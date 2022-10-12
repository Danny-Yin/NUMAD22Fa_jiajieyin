package edu.northeastern.NUMAD22Fa_jiajieyin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PrimePageMain extends AppCompatActivity {
    Button findPrimes;
    Button terminateSearch;
    TextView primeNumberText;

    TextView primeOutput;

    int lastPrimeNumber;

    boolean runPrimeThread = false;

    Handler textHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prime_page);

        primeNumberText = findViewById(R.id.textViewPrime);
        primeOutput = findViewById(R.id.searchPrime);

        findPrimes = findViewById(R.id.findPrime);

        findPrimes.setOnClickListener(v ->{
            if (!runPrimeThread){
                runPrimeThread = true;
                runPrimesOnRunnableThread();
            } else {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, "Primes Number", duration);
                toast.show();
            }

        });

        terminateSearch = findViewById(R.id.terminateSearch);

        terminateSearch.setOnClickListener(v -> {
            if (runPrimeThread){
                runPrimeThread = false;
                primeNumberText.setText("output: \n" + lastPrimeNumber);
            }
            primeOutput.setText("");
        });

    }


    public void runPrimesOnRunnableThread() {
        PrimeRunnableThread prt = new PrimeRunnableThread();
        new Thread(prt).start();
    }

    private boolean isPrime(int n) {
        for (int i = 2; i <= n/2; i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    class PrimeRunnableThread implements Runnable{

        @Override
        public void run() {

            int counter = 3;

            while (counter < Integer.MAX_VALUE && runPrimeThread){
                final int res = counter;
                textHandler.post(() ->{
                    if (isPrime(res)){
                        lastPrimeNumber = res;
                        primeOutput.setText("Current number: " + res);
                        primeNumberText.setText("Latest Prime Number: " + lastPrimeNumber);
                    } else {
                        primeOutput.setText("Current number being checked: " + res);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    runPrimeThread = false;
                    e.printStackTrace();
                }
                counter++;
            }
                }
            }
    @Override
    public void onBackPressed() {

        if (runPrimeThread) {
            alertMsg("Are you sure you want to exit?");
            if (!runPrimeThread) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    private void alertMsg(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(PrimePageMain.this);
        alert.setTitle(msg);
        alert.setPositiveButton("Yes", (dialog, which) -> {
            runPrimeThread = false;
            dialog.dismiss();
            finish();
        });
        alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        alert.create().show();
    }

}
