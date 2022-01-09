package my.edu.utar.e_votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminReport extends AppCompatActivity {

    private AnyChartView anyChartView;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);

        anyChartView = findViewById(R.id.any_chart_view);
        ref = FirebaseDatabase.getInstance().getReference().child("Vote");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get option name
                String name1 = snapshot.child("Option1").child("Name").getValue().toString();
                String name2 = snapshot.child("Option2").child("Name").getValue().toString();
                int amount1 = Integer.parseInt(snapshot.child("Option1").child("Amount").getValue().toString());
                int amount2 = Integer.parseInt(snapshot.child("Option2").child("Amount").getValue().toString());
                String[] optionName = {name1, name2};
                int[] optionAmount = {amount1, amount2};
                setupPieChart(optionName,optionAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void setupPieChart(String[] optionName, int[] optionAmount) {

        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for(int i = 0; i<optionName.length;i++){
            dataEntries.add(new ValueDataEntry(optionName[i],optionAmount[i]));
        }

        pie.data(dataEntries);
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Vote Option")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView.setChart(pie);
    }
}