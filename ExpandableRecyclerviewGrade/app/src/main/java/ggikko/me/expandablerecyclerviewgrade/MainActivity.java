package ggikko.me.expandablerecyclerviewgrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ggikko on 16. 4. 21..
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerview) RecyclerView recyclerView;

    List<RecyclerViewAdapter.GradeListItem> listItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        prepareDate();

        adapter.setItems(listItemList);
    }

    private void prepareDate() {
        
    }
}
