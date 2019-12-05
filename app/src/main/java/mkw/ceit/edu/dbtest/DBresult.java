package mkw.ceit.edu.dbtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBresult extends AppCompatActivity {
    private DatabaseAccess databaseAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbresult);
        String sql = getIntent().getStringExtra("sql");
        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        Cursor cursor = databaseAccess.getRequestedData(sql);
        show(cursor);
        databaseAccess.close();


    }

    void show(Cursor result) {

        try {
         //   JSONArray jArray = new JSONArray(result);

            TableLayout tv = (TableLayout) findViewById(R.id.rstable);
            tv.setPadding(20,10,10,0);
            tv.removeAllViewsInLayout();
            int flag = 1;
            result.moveToFirst();
            // when i=-1, loop will display heading of each column
            // then usually data will be display from i=0 to jArray.length()
            for (int i = -1; i < result.getCount(); i++) {

                TableRow tr = new TableRow(this);

                tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                tr.setPadding(20,10,10,0);

                // this will be executed once
                if (flag == 1) {

                   for (int a = 0;a<result.getColumnCount();a++)
                   {
                       TextView textView = new TextView(this);
                       textView.setText(result.getColumnName(a));
                       textView.setTextColor(Color.BLUE);
                       textView.setTextSize(15);
                       textView.setPadding(50,20,0,10);
                       tr.addView(textView);

                   }
                    tv.addView(tr);
                    final View vline = new View(this);
                    vline.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
                    vline.setBackgroundColor(Color.BLUE);
                    tv.addView(vline); // add line below heading
                    flag = 0;
                } else {
                    for (int index = 0;index<result.getColumnCount();index++)
                    {
                        TextView b = new TextView(this);
                        String str = String.valueOf(cursorTypeCheck(index,result));
                        b.setText(str);
                        b.setTextColor(Color.RED);
                        b.setTextSize(15);
                        b.setPadding(50,20,0,10);
                        tr.addView(b);
                    }

                    tv.addView(tr);
                    final View vline1 = new View(this);
                    vline1.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    vline1.setBackgroundColor(Color.BLACK);
                    tv.addView(vline1);  // add line below each row
                    result.moveToNext();
                }


            }


        }catch (Exception e1)
        {
            Toast.makeText(getApplicationContext(),"kdljkf",Toast.LENGTH_SHORT).show();
        }
    }

    String cursorTypeCheck(int a,Cursor cursor)
    {
        String string = "";
        switch (cursor.getType(a)) {
            case Cursor.FIELD_TYPE_FLOAT:
                string =  String.valueOf(cursor.getFloat(a));
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                string   = String.valueOf(cursor.getInt(a));
                break;
            case Cursor.FIELD_TYPE_STRING:
                string  = String.valueOf(cursor.getString(a));
                break;
        }
        return string;

    }

}
