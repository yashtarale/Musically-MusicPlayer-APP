package com.example.musically;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ListView listView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);

//1..............................................................
//INCLUDED A LISTvIEW IN ACTIVITY
//        permissions
//also check manifest include his : <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

        //make a permission box...to take permission....by dexter library
        //https://github.com/Karumi/Dexter
        // implementation 'com.karumi:dexter:6.2.3' copy paste to build.gradle

        // insert this  for permission here
        //Dexter.withContext(activity)
        //			.withPermission(permission)
        //			.withListener(listener)
        //			.check();


        //2..................................................................................................
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "runtime permission given", Toast.LENGTH_SHORT).show();
                        //4444444444444444444444..............................................
                        ArrayList<File> mySongs =fetchSongs(Environment.getExternalStorageDirectory());
                        String [] items =new String[mySongs.size()];
                        for (int i =0;i<mySongs.size();i++){
                            items[i]=mySongs.get(i).getName().replace(".mp3","");
                        }
                        //555555555555555555..........................................................
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);
                        //6666666666666666666666666666666.....................................................
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this,playSong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList",mySongs);
                                intent.putExtra("currentSong",currentSong);
                                intent.putExtra("position",position);
                                startActivity(intent);
                            }
                        });


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();


//         close of permossion


    }

//3333333333333333333333333333333333..........................................................
//    return directories mp3 files after permission by user
        public ArrayList<File> fetchSongs(File file){
        ArrayList arrayList =new ArrayList();
        File [] songs = file.listFiles();
        if(songs!=null){
            for(File myFile: songs){
                if (!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else {
                    if (myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;

}

}