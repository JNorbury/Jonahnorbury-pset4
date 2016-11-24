package jnorbury.jonahnorbury_pset4;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public DBHelper dbhelp;
    public SQLiteDatabase db;
    private ListView tasklistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showAll();
    }

    private void showAll() {
        dbhelp = new DBHelper(this);

//        String[] dummydata = {"Drink water", "Sniff coke", "Get Paid"};

        tasklistview = (ListView) findViewById(R.id.tasklistview);
        final ArrayList<String> tasklist = new ArrayList<>();

//        for (int i = 0; i < dummydata.length; i++) {
//            tasklist.add(i, dummydata[i]);
//        }

        final ArrayList<HashMap<String, String>> dbtasks = dbhelp.read();
        for (int i = 0; i < dbtasks.size(); i++) {
            final HashMap<String, String> hm = dbtasks.get(i);
            String s = hm.get("task_name");
            tasklist.add(i, s);
        }


        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, tasklist);
        tasklistview.setAdapter(adapter);


        tasklistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view,
                                        final int position, long id){
                String s = tasklistview.getItemAtPosition(position).toString();
                s = s.replaceFirst(" .*", "");
                dbhelp.delete(Integer.parseInt(s));

                adapter.notifyDataSetChanged();

                showAll();

                return true;
            }
        });
    }

    // optional method onResume
//    @Override
//    protected void onResume(Bundle savedInstanceState) {
//        super.onResume();
//        setContentView(R.layout.activity_main);
//    }

    public void addNewToDoTask(View view) {
        EditText edittext = (EditText) findViewById(R.id.editText);
        ToDoTask currentToDoTask = new ToDoTask();
        currentToDoTask.setToDoTask_name(edittext.getText().toString());
        dbhelp.create(currentToDoTask);

        showAll();

        edittext.setText("");
    }
}
