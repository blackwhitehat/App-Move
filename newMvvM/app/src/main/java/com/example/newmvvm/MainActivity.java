package com.example.newmvvm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.newmvvm.AllLink.AllMove;
import com.example.newmvvm.AllLink.PAdapter.AllProduct;
import com.example.newmvvm.AppController.AppController;

import com.example.newmvvm.Details.Detail;
import com.example.newmvvm.Move.Adapter.MAdapter;

import com.example.newmvvm.Move.MainViewModel;

import com.example.newmvvm.Move.Pro.Link;
import com.example.newmvvm.Move.Pro.Search;
import com.example.newmvvm.Move.Save_Info;
import com.example.newmvvm.Spl.Splash;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity  {


    private ProgressDialog pd;

    private MainViewModel viewModel;
    private String poster[];
    private RecyclerView recyclerView;
    private MAdapter adapter;
    private List<Link> data=new ArrayList<>();
    private CarouselView carouselView;
    private Save_Info saveInfo;
    private TextView allpro;
    private CompositeDisposable disposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxJavaPlugins.setErrorHandler(throwable -> {});
        setContentView(R.layout.activity_main);




       saveInfo=new Save_Info(this);


        set();
        new AA().execute();


       /* if (saveInfo.GetInfo().equals("null"))
        {
            startActivity(new Intent(getApplicationContext(), Splash.class));
        }else{


        }*/



allpro.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(), AllMove.class));
    }
});


    }

    private void getAll()
    {
        viewModel=new MainViewModel();
        viewModel.getList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Link>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                            disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Link link) {
                        poster=new String[link.getSearch().size()];
                      for (int i=0;i<link.getSearch().size();i++) {
                          data.add(link);

                          poster[i]=link.getSearch().get(i).getPoster();
                      }
                       adapter=new MAdapter(MainActivity.this,data);
                       recyclerView.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                        //Toast.makeText(MainActivity.this, poster.length+"", Toast.LENGTH_SHORT).show();
                        carouselView=findViewById(R.id.carouselView);
                        carouselView.setImageListener(imageListen);
                        carouselView.setPageCount(poster.length);
                        adapter.setPrime(new MAdapter.OnPrime() {
                            @Override
                            public void ocClick(String ids) {
                                Intent dt=new Intent(MainActivity.this, Detail.class);
                                dt.putExtra("key",link.getSearch().get(Integer.parseInt(ids)).getImdbID());
                                startActivity(dt);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("tag","err->"+e.getMessage());
                    }
                });
    }

    ImageListener imageListen=new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
        Picasso.get().load(poster[position]).networkPolicy(NetworkPolicy.OFFLINE).fit().into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
            //Picasso.get().load(poster[position]).networkPolicy(NetworkPolicy.OFFLINE).fit().into(imageView);
        }
    };

    private void set()
    {
        carouselView=findViewById(R.id.carouselView);
        allpro=findViewById(R.id.main_all);
        recyclerView=findViewById(R.id.main_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));

    }







    public class AA extends AsyncTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(MainActivity.this);
            pd.setTitle("wait.");
            pd.setMessage("Get Data Server...");
            pd.setCancelable(true);
            pd.setIcon(R.drawable.ic_cloud);
            pd.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

                pd.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }


}

