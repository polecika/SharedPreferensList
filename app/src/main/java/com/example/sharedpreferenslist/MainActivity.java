package com.example.sharedpreferenslist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    SharedPreferences saveBuf;
    private static int deleteNumber;
    final String SAVED_TEXT = "saved_text";
    List<Map<String, String>> values;
    private static int idNumber;
    SimpleAdapter listContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveBuf = getPreferences(MODE_PRIVATE);
        if (saveBuf.getString(SAVED_TEXT, "").equals(saveBuf.getString(SAVED_TEXT, null))) {
            sharedPreferensSave();
        }
        ListView list = findViewById(R.id.listView);
        values = prepareContent();

        listContentAdapter = createAdapter(values);

        list.setAdapter(listContentAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                values.remove(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });


        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swipeRefreshLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                values = prepareContent();
                listContentAdapter = createAdapter(values);
                ListView list = findViewById(R.id.listView);
                list.setAdapter(listContentAdapter);
                swipeLayout.setRefreshing(false);

            }
        });
        swipeLayout.setRefreshing(false);
    }

    @NonNull
    private SimpleAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values, R.layout.text_1_2, new String[]{KEY1, KEY2}, new int[]{R.id.text1, R.id.text2});
    }

    @NonNull
    private List<Map<String, String>> prepareContent() {
        List<Map<String, String>> result = new ArrayList<>();
        String[] words;
        saveBuf = getPreferences(MODE_PRIVATE);
        words = saveBuf.getString(SAVED_TEXT, "").split("\n\n");
        for (int i = 0; i < words.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY1, words[i]);
            map.put(KEY2, words[i].length() + "");
            result.add(map);
        }
        return result;
    }

    private List<Map<String, String>> deleteContent(int id) {
        List<Map<String, String>> result = new ArrayList<>();
        String[] words;
        words = getString(R.string.large_text).split("\n\n");
        ArrayList<String> wordsAfterDelete = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (i == id) {
                break;
            }
            wordsAfterDelete.add(words[i]);
        }
        for (int i = 0; i < words.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY1, wordsAfterDelete.get(i));
            map.put(KEY2, wordsAfterDelete.get(i).length() + "");
            result.add(map);
        }
        return result;
    }

    public void sharedPreferensSave() {
        saveBuf = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = saveBuf.edit();
        ed.putString(SAVED_TEXT, getString(R.string.large_text));
        ed.commit();

    }
}