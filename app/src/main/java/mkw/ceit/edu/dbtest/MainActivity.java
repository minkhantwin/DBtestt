package mkw.ceit.edu.dbtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private int constaintviewId;
    private ConstraintLayout linearLayout;
    private int pieId = 1;
    private int sigId = 2;
    private int nameId = 1000;
    private int spinnerId = 100;
    private int layoutId = 200;
    private EditText first_tbname;
    ArrayList<Integer> pieMetaData = new ArrayList<>();
    ArrayList<Integer> sigMetaData = new ArrayList<>();
    ArrayList<Integer> tableMetaData = new ArrayList<>();
    ArrayList<String> andorMetadata = new ArrayList<>();
    ArrayList<String> traceArray = new ArrayList<>();
    ArrayList<Integer> layouttrace = new ArrayList<>();

    private Button and_button;
    private Button or_button;

    private  DatabaseAccess databaseAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseAccess = DatabaseAccess.getInstance(this);
        first_tbname = findViewById(R.id.first_tbname);
         linearLayout = findViewById(R.id.linearLayout);
         constaintviewId = first_tbname.getId();
         layouttrace.add(constaintviewId);
         traceArray.add("start");
         andorMetadata.add("false");
    }

    public void PieFunction(View view) {

        Toast.makeText(getApplicationContext(),"Pie",Toast.LENGTH_SHORT).show();

        LinearLayout viewlayout = new LinearLayout(this);
        viewlayout.setId(layoutId++);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewlayout.setLayoutParams(layoutParams);
        viewlayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView tview = new TextView(this);
        tview.setText("Pie");

        LinearLayout.LayoutParams tviewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tviewParams.setMargins(50,20,10,0);
        tview.setLayoutParams(tviewParams);
        tview.setTextSize(20);
      //  tview.setId(id[1]);

        EditText etext = new EditText(this);
        ConstraintLayout.LayoutParams etextParams = new ConstraintLayout.LayoutParams(
                300,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        etextParams.setMargins(10,10,0,0);

        etext.setLayoutParams(etextParams);
        etext.setId(pieId);
        pieMetaData.add(pieId);
        pieId = pieId +2;
        viewlayout.addView(tview);
        viewlayout.addView(etext);

        ConstraintSet set = new ConstraintSet();

        linearLayout.addView(viewlayout,0);
        set.clone(linearLayout);
        set.connect(viewlayout.getId(), ConstraintSet.BOTTOM, constaintviewId , ConstraintSet.TOP, 20);
        set.applyTo(linearLayout);
        constaintviewId = viewlayout.getId();
        //linearLayout.addView(viewlayout);
        layouttrace.add(constaintviewId);
        traceArray.add("pie");
    }

    public void NameFunction(View view) {
        EditText etext = new EditText(this );
        LinearLayout.LayoutParams etextparam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        etextparam.setMargins(50,10,90,0);
        etext.setLayoutParams(etextparam);
        etext.setId(nameId);
        tableMetaData.add(nameId++);
        linearLayout.addView(etext);

        traceArray.add("name");

    }

    public void SigFunction(View view) {
        if(traceArray.get(traceArray.size()-1) != "sig")
        {
            addNewSig();
        }
        else {
            addSigAndOr();
        }

    }

    public void RunSql(View view) {
        String sql = "select ";
        if(pieMetaData.isEmpty())
        {
            sql = sql + "* from ";
        }else {
            for(int i = 0;i<pieMetaData.size();i++)
            {
                int id = pieMetaData.get(i);
                EditText editText = findViewById(id);
                String s = String.valueOf(editText.getText());
                sql = sql + s;
                if(!(i == pieMetaData.size()-1))
                    sql = sql+",";
                else
                    sql = sql + " from ";

            }

        }
        int andor = 0;
     //   EditText tableText = findViewById(tableMetaData.get(0));
        sql = sql + String.valueOf(first_tbname.getText());
        if(!sigMetaData.isEmpty())
            sql = sql + " where ";
        for(int i = 0;i<sigMetaData.size();i++)
        {
            EditText editText1 = findViewById(sigMetaData.get(i++));
            String s1 = String.valueOf(editText1.getText());
            Spinner spinner = findViewById(sigMetaData.get(i++));
            EditText editText2 = findViewById(sigMetaData.get(i));
            String s2 = String.valueOf(editText2.getText());

            if(spinner.getSelectedItem() == "=")
                sql = sql + s1 + "=" + s2;
            else if (spinner.getSelectedItem() == "<")
                sql = sql + s1 + "<" + s2;
            else if (spinner.getSelectedItem() == ">")
                sql = sql + s1 + ">" + s2;
            if(andorMetadata.get(andor) == "true")
                sql = sql + " "+ andorMetadata.get(++andor)+" ";


        }
       Intent intent = new Intent(MainActivity.this,DBresult.class);
        intent.putExtra("sql", sql);
        startActivity(intent);

     }

     void addNewSig() {

       //  int id = 100;
         LinearLayout viewlayout = new LinearLayout(this);
         viewlayout.setId(layoutId++);
         viewlayout.setOrientation(LinearLayout.HORIZONTAL);
         TextView tview = new TextView(this);
         tview.setText("Sig");

         LinearLayout.LayoutParams tviewParams = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
         tviewParams.setMargins(60,20,10,0);
         tview.setLayoutParams(tviewParams);
         tview.setTextSize(20);
      //   tview.setId(id);

         Spinner compareSpinner = new Spinner(this);
         ArrayList<String> spinnerArray = new ArrayList<>();
         spinnerArray.add("=");
         spinnerArray.add(">");
         spinnerArray.add("<");
         //    spinnerArray.add(" != ");
         LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
         spinnerParams.setMargins(20,20,0,0);
         compareSpinner.setLayoutParams(spinnerParams);

         ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerArray);
         compareSpinner.setAdapter(stringArrayAdapter);

         EditText etext1  = new EditText(this);
         EditText etext2  = new EditText(this);
         LinearLayout.LayoutParams etextParams = new LinearLayout.LayoutParams(
                 300,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
         etextParams.setMargins(0,20,0,0);
         etext1.setLayoutParams(etextParams);
         etext2.setLayoutParams(etextParams);
         etext2.setInputType(InputType.TYPE_CLASS_NUMBER);
         etext1.setId(sigId);
         sigMetaData.add(sigId);
         sigId = sigId +2;
         compareSpinner.setId(sigId);
         sigMetaData.add(sigId);
         sigId = sigId +2;
         etext2.setId(sigId);
         sigMetaData.add(sigId);
         sigId = sigId +2;

         viewlayout.addView(tview);
         viewlayout.addView(etext1);
         viewlayout.addView(compareSpinner);
         viewlayout.addView(etext2);

         ConstraintSet set = new ConstraintSet();

         linearLayout.addView(viewlayout,0);
         set.clone(linearLayout);
         set.connect(viewlayout.getId(), ConstraintSet.BOTTOM, constaintviewId , ConstraintSet.TOP, 20);
         set.applyTo(linearLayout);
         constaintviewId = viewlayout.getId();

         layouttrace.add(constaintviewId);
         traceArray.add("sig");

    }


     void addSigAndOr()
     {

         final ConstraintLayout parentlayout = findViewById(R.id.main);

         disableEnableControls(false, parentlayout);
         final PopupWindow popupWindow = new PopupWindow(this);
         LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
         View view = layoutInflater.inflate(R.layout.and_or_cardview, null);

         popupWindow.setContentView(view);
         popupWindow.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);
         //    popupWindow.setFocusable(false);
         //   popupWindow.setOutsideTouchable(false);
         and_button = view.findViewById(R.id.select_and);
         or_button = view.findViewById(R.id.select_or);
         and_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 and_or_sig("AND");
                 andorMetadata.set(0,"true");
                 andorMetadata.add("AND");
                 popupWindow.dismiss();
             }
         });
         or_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 and_or_sig("OR");
                 andorMetadata.set(0,"true");
                 andorMetadata.add("OR");
                 popupWindow.dismiss();
             }
         });
         parentlayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 popupWindow.dismiss();
             }
         });
         disableEnableControls(true, parentlayout);

     }

    private void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }

    void and_or_sig(String s)
    {
        int id = 100;
        LinearLayout viewlayout = new LinearLayout(this);
        viewlayout.setId(layoutId++);
        viewlayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView tview = new TextView(this);
        tview.setText(s);

        LinearLayout.LayoutParams tviewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tviewParams.setMargins(100,20,10,0);
        tview.setLayoutParams(tviewParams);
        tview.setTextSize(20);
        tview.setId(id);

        Spinner compareSpinner = new Spinner(this);
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("=");
        spinnerArray.add(">");
        spinnerArray.add("<");
        //    spinnerArray.add(" != ");
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        spinnerParams.setMargins(20,20,0,0);
        compareSpinner.setLayoutParams(spinnerParams);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerArray);
        compareSpinner.setAdapter(stringArrayAdapter);

        EditText etext1  = new EditText(this);
        EditText etext2  = new EditText(this);
        LinearLayout.LayoutParams etextParams = new LinearLayout.LayoutParams(
                300,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        etextParams.setMargins(0,20,0,0);
        etext1.setLayoutParams(etextParams);
        etext2.setLayoutParams(etextParams);
        etext2.setInputType(InputType.TYPE_CLASS_NUMBER);
        etext1.setId(sigId);
        sigMetaData.add(sigId);
        sigId = sigId +2;
        compareSpinner.setId(sigId);
        sigMetaData.add(sigId);
        sigId = sigId +2;
        etext2.setId(sigId);
        sigMetaData.add(sigId);
        sigId = sigId +2;

        viewlayout.addView(tview);
        viewlayout.addView(etext1);
        viewlayout.addView(compareSpinner);
        viewlayout.addView(etext2);

        ConstraintSet set = new ConstraintSet();

        linearLayout.addView(viewlayout,0);
        set.clone(linearLayout);
        set.connect(viewlayout.getId(), ConstraintSet.BOTTOM, layouttrace.get(layouttrace.size()-2) , ConstraintSet.TOP, 20);
        set.connect(constaintviewId, ConstraintSet.BOTTOM, viewlayout.getId() , ConstraintSet.TOP, 20);
        set.applyTo(linearLayout);
        constaintviewId = viewlayout.getId();
        layouttrace.add(constaintviewId);
        traceArray.add("sig");

    }


    public void removeView(View view) {
        int a = traceArray.size() - 1;
        int s = sigMetaData.size() -1;
        int p = pieMetaData.size() - 1;
        layoutId = layoutId -1;
        if( layoutId >= 200)
        {
            LinearLayout removeV = findViewById(layoutId);
            linearLayout.removeView(removeV);

            if (traceArray.get(a) == "pie")
            {
                pieId = pieId - 2;
                pieMetaData.remove(p);
            }else  if(traceArray.get(a) == "sig")
            {
                if(traceArray.get(a-1) == "sig")
                {
                    ConstraintSet set = new ConstraintSet();
                    set.clone(linearLayout);
                    set.connect(constaintviewId-1, ConstraintSet.BOTTOM, layouttrace.get(layouttrace.size()-3) , ConstraintSet.TOP, 20);
                    set.applyTo(linearLayout);
                    andorMetadata.set(0,"false");
                }

                sigId = sigId - 6;
                sigMetaData.remove(s);
                sigMetaData.remove(s-1);
                sigMetaData.remove(s-2);
            }
            traceArray.remove(traceArray.size()-1);
            layouttrace.remove(layouttrace.size()-1);
            constaintviewId--;

        }else {
            layoutId++;
        }
        if(layoutId  == 200)
            constaintviewId = first_tbname.getId();

    }


}
