package com.hfad.music;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;

import java.io.IOException;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
        private static final int MY_PERMISSION_REQUEST=1;
        ArrayList<String> arrayList;
        ListView listView;
        ArrayAdapter<String> adapter;
        private Sheets service=null;
        List<Object> list= new List<Object>() {
                @Override
                public int size() {
                        return 0;
                }

                @Override
                public boolean isEmpty() {
                        return false;
                }

                @Override
                public boolean contains(Object o) {
                        return false;
                }

                @NonNull
                @Override
                public Iterator<Object> iterator() {
                        return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                        return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(@NonNull T[] ts) {
                        return null;
                }

                @Override
                public boolean add(Object o) {
                        return false;
                }

                @Override
                public boolean remove(Object o) {
                        return false;
                }

                @Override
                public boolean containsAll(@NonNull Collection<?> collection) {
                        return false;
                }

                @Override
                public boolean addAll(@NonNull Collection<?> collection) {
                        return false;
                }

                @Override
                public boolean addAll(int i, @NonNull Collection<?> collection) {
                        return false;
                }

                @Override
                public boolean removeAll(@NonNull Collection<?> collection) {
                        return false;
                }

                @Override
                public boolean retainAll(@NonNull Collection<?> collection) {
                        return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public Object get(int i) {
                        return null;
                }

                @Override
                public Object set(int i, Object o) {
                        return null;
                }

                @Override
                public void add(int i, Object o) {

                }

                @Override
                public Object remove(int i) {
                        return null;
                }

                @Override
                public int indexOf(Object o) {
                        return 0;
                }

                @Override
                public int lastIndexOf(Object o) {
                        return 0;
                }

                @NonNull
                @Override
                public ListIterator<Object> listIterator() {
                        return null;
                }

                @NonNull
                @Override
                public ListIterator<Object> listIterator(int i) {
                        return null;
                }

                @NonNull
                @Override
                public List<Object> subList(int i, int i1) {
                        return null;
                }
        };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)){
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
                        }else{
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
                        }

                }else
                {
                        try {
                                doStuff();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
        public void doStuff() throws IOException {
                listView= findViewById(R.id.list);
                arrayList=new ArrayList<>();
                getMusic();
                adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        }
                });

        }
        public void getMusic() throws IOException {
                ContentResolver contentResolver = getContentResolver();
                Uri songuri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor songCorsor = contentResolver.query(songuri,null,null,null,null);
                if(songCorsor != null && songCorsor.moveToFirst()){
                        int songTitle = songCorsor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                        int songArtist = songCorsor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

                        do {
                                String currentTitle = songCorsor.getString(songTitle);
                                String currentArtist = songCorsor.getString(songArtist);
                                list.add(currentTitle + "\n" + currentArtist);
                                arrayList.add(currentTitle + "\n" + currentArtist);
                        }while (songCorsor.moveToNext());

                        List<List<Object>> values = Arrays.asList(
                                Arrays.asList(

                                        new Object[]{"hello world"}   // Cell values ...
                                )
                                // Additional rows ...
                        );
                        com.google.api.services.sheets.v4.model.ValueRange body = new com.google.api.services.sheets.v4.model.ValueRange()
                                .setValues(values);
                        UpdateValuesResponse result =
                                service.spreadsheets().values().update("1tFxOIqRWQ4arC_H7rsw-gcAyHBjqlgJsJYzygvksww4", "music!A:B", body)
                                        .setValueInputOption("RAW")
                                        .execute();
                        System.out.printf("%d cells updated.", result.getUpdatedCells());
                }
        }
        @Override
        public void onRequestPermissionsResult(int requestcode,String[] permissions, int[] grantResults){
                switch (requestcode){
                        case MY_PERMISSION_REQUEST:
                        {
                                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                                        if(ContextCompat.checkSelfPermission(MainActivity.this,
                                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                                Toast.makeText(this,"permission granted!",Toast.LENGTH_SHORT).show();

                                                try {
                                                        doStuff();
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }else {
                                        Toast.makeText(this,"no permission Granted",Toast.LENGTH_SHORT).show();
                                        finish();
                                }
                                return;
                        }
                }
        }



}