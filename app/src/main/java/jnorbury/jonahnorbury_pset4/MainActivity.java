package jnorbury.jonahnorbury_pset4;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public DBHelper dbhelp;
    private ListView tasklistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showAll();
    }

    private void showAll() {
        dbhelp = new DBHelper(this);

        tasklistview = (ListView) findViewById(R.id.tasklistview);
        final ArrayList<String> tasklist = new ArrayList<>();

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

    public void addNewToDoTask(View view) {
        EditText edittext = (EditText) findViewById(R.id.editText);
        String s = edittext.getText().toString();
        if (s.matches("")) {
            Toast.makeText(this, jnorbury.jonahnorbury_pset4.R.string.no_task_given_toast_text, Toast.LENGTH_SHORT).show();
            return;
        }
        ToDoTask currentToDoTask = new ToDoTask();
        currentToDoTask.setToDoTask_name(edittext.getText().toString());
        dbhelp.create(currentToDoTask);

        showAll();
        edittext.setText("");
    }
}
