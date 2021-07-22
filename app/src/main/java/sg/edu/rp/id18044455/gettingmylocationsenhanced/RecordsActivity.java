package sg.edu.rp.id18044455.gettingmylocationsenhanced;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    ActionBar ab;
    TextView tvRecords;
    Button btnRefresh, btnFavourites;
    ListView lvRecords;
    ArrayAdapter aa;
    ArrayList<String> records, favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        tvRecords = findViewById(R.id.tvRecords);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnFavourites = findViewById(R.id.btnFavourites);
        lvRecords = findViewById(R.id.lvRecords);
        records = new ArrayList<>();
        favourites = new ArrayList<>();

        aa = new ArrayAdapter(RecordsActivity.this, android.R.layout.simple_list_item_1, records);
        lvRecords.setAdapter(aa);


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/L10PSFolder";
                File targetFile = new File(folderLocation, "records.txt");
                if (targetFile.exists()) {
                    records.clear();
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            records.add(line);
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                        aa = new ArrayAdapter(RecordsActivity.this, android.R.layout.simple_list_item_1, records);
                        lvRecords.setAdapter(aa);
                        tvRecords.setText("Number of records: " + records.size());
                    } catch (Exception e) {
                        Toast.makeText(RecordsActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

        });

        lvRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(RecordsActivity.this);
                myBuilder.setMessage("Add this location in your favourite list?");
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/L10PSFolder";
                            File targetFile = new File(folderLocation, "favourites.txt");
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(records.get(position) + "\n");
                            writer.flush();
                            writer.close();
                            Toast.makeText(RecordsActivity.this, "Location added to your favourites", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                myBuilder.setNegativeButton("No", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

        btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/L10PSFolder";
                File targetFile = new File(folderLocation, "favourites.txt");
                if (targetFile.exists()) {
                    favourites.clear();
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            favourites.add(line);
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                        aa = new ArrayAdapter(RecordsActivity.this, android.R.layout.simple_list_item_1, favourites);
                        lvRecords.setAdapter(aa);
                        tvRecords.setText("Number of records: " + favourites.size());
                    } catch (Exception e) {
                        Toast.makeText(RecordsActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });




    }//end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/L10PSFolder";
        File targetFile = new File(folderLocation, "records.txt");
        if (targetFile.exists()) {
            records.clear();
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    records.add(line);
                    line = br.readLine();
                }
                br.close();
                reader.close();
                aa.notifyDataSetChanged();
                tvRecords.setText("Number of records: " + records.size());
            } catch (Exception e) {
                Toast.makeText(RecordsActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }//end of onResume
}//end of class