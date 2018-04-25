package vn.dattran.family.Activity;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vn.dattran.family.Custom.CustomBehavior;
import vn.dattran.family.Custom.CustomView;
import vn.dattran.family.Model.Person;
import vn.dattran.family.R;

public class MainActivity extends AppCompatActivity {
    List<Person> lsPerson;
    CustomView customView;
    String url="http://www.phimmoi.net/phim/trum-huong-cang-6402/xem-phim.html";
    TextView textView;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    NestedScrollView nestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        customView = findViewById(R.id.my_custom);
//        loadData();
//        customView.setOnItemClick(new CustomView.OnItemClick() {
//            @Override
//            public void onClick(Person person) {
//                Toast.makeText(MainActivity.this, person.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
        test();
    }

    private void loadData() {
        try {
            InputStream is = getResources().getAssets().open("family.json");
            Type type = new TypeToken<ArrayList<Person>>() {
            }.getType();
            InputStreamReader isr = new InputStreamReader(is);
            lsPerson = new Gson().fromJson(isr, type);
            isr.close();
            customView.setData(lsPerson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void test(){
        textView=findViewById(R.id.textview);
        Toast.makeText(MainActivity.this, "Bat dau", Toast.LENGTH_SHORT).show();
        nestedScrollView=findViewById(R.id.nested);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Document doc= Jsoup.connect(url).get();
                    return doc.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                textView.setText(s);
                Toast.makeText(MainActivity.this, "ket thu", Toast.LENGTH_SHORT).show();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        final BottomNavigationView bottomNavigationView=findViewById(R.id.navigation);
        CoordinatorLayout.LayoutParams params= (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        params.setBehavior(new CustomBehavior());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_shop:
                        toolbar.setTitle(item.getTitle());
                        return true;
                    case R.id.navigation_gifts:
                        toolbar.setTitle("My Gifts");
                        return true;
                    case R.id.navigation_cart:
                        toolbar.setTitle("Cart");
                        return true;
                    case R.id.navigation_profile:
                        toolbar.setTitle("Profile");
                        return true;
                }
                return false;
            }
        });

//        ((AppBarLayout)toolbar.getParent()).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                bottomNavigationView.setTranslationY(verticalOffset*-1);
//            }
//        });
//        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            int dy,temp=0;
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.d("thunghiem",""+scrollY+", ");
//                dy=scrollY-oldScrollY;
//                if (dy>0){
//                    if (temp<bottomNavigationView.getHeight()){
//                        temp++;
//                        bottomNavigationView.setTranslationY(temp);
//                    }
//                }else {
//                    if (temp>0){
//                        temp--;
//                        bottomNavigationView.setTranslationY(temp);
//                    }
//                }
//            }
//        });

    }




    public static final void print(Object... obj) {
        String s = obj[0].toString();
        for (int i = 1; i < obj.length; i++) {
            s += ", " + obj[i].toString();
        }
        Log.d("thunghiem", s);
    }
}
