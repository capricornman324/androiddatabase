package stikombali.ac.id.latihandatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import stikombali.ac.id.latihandatabase.adapter.ContactAdapter;
import stikombali.ac.id.latihandatabase.entity.ContactPerson;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lstData)
    ListView listContact;

    List<ContactPerson> contacts = new ArrayList<ContactPerson>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
      //  loadContact();
        listContact = (ListView)findViewById(R.id.lstData);
        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactPerson cp = (ContactPerson)listContact.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);

                intent.putExtra("ID",cp.getId());
                startActivity(intent);
            }
        });

        registerForContextMenu(listContact); //pertama untuk context menu
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContact();
    }
    private void loadContact(){
        contacts = ContactPerson.listAll(ContactPerson.class);
        listContact.setAdapter(new ContactAdapter(this,contacts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuInsert :
                Intent intent = new Intent(this,InputActifity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return  true;
    }

    //contex menu mulai
    @Override
    public void onCreateContextMenu (ContextMenu menu,View view,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete,menu);
    }

    @Override
    public boolean onContextItemSelected (MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.menuDelete:
                ContactPerson cp = (ContactPerson)listContact.getItemAtPosition(info.position);
                cp.delete();
                Toast.makeText(this,"Data terhapus",Toast.LENGTH_LONG).show();
                loadContact();
                return  true;
            default:
                return  super.onContextItemSelected(item);
        }
    }
    //context menu selesai


}
