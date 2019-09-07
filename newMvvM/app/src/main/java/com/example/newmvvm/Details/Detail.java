package com.example.newmvvm.Details;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.newmvvm.Details.ModelDetail.DetailList;
import com.example.newmvvm.Details.ModelDetail.Rating;
import com.example.newmvvm.MainActivity;
import com.example.newmvvm.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

public class Detail extends AppCompatActivity {

    private String[] title;
    ProgressDialog pd;
    private TextView writer,point;
    private RatingBar ratingBar;
    private TextView det_txt,det_country,det_yr,det_direct;
    private ImageView det_poster;
    private FloatingActionButton fab;
    private  Bundle ex;
    private CompositeDisposable disposable=new CompositeDisposable();
    private DetailViewModel viewModel=new DetailViewModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        set();

        ex=getIntent().getExtras();
       // Toast.makeText(this, ex.getString("key")+"", Toast.LENGTH_SHORT).show();
        CollapsingToolbarLayout ct = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ct.setTitle("MoveLinkDoni");

        new AA().execute();

    }


    private void getDetails()
    {
       // Toast.makeText(this, ex.getString("key")+"", Toast.LENGTH_SHORT).show();
        viewModel.getDetailoList("3e974fca",ex.getString("key")).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DetailList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(DetailList detailLists) {

                        det_txt.setText(detailLists.getTitle());
                        det_country.setText(detailLists.getCountry());
                        det_yr.setText(detailLists.getRuntime()+"/"+detailLists.getYear());
                        det_direct.setText(detailLists.getDirector());
                        writer.setText(detailLists.getWriter());
                        if (!writer.getText().equals("null") || !writer.getText().equals(""))
                            pd.dismiss();
                        Picasso.get().load(detailLists.getPoster()).networkPolicy(NetworkPolicy.OFFLINE).fit().into(det_poster);

                        ratingBar.setRating(Float.parseFloat(detailLists.getRatings().get(1).getValue().substring(0,3)));
                        point.setText(detailLists.getRatings().get(0).getValue().substring(0,3));


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("tag","err->"+e.getMessage());
                    }
                });
    }


    private void set() {
        writer=findViewById(R.id.det_writer);
        det_txt=findViewById(R.id.det_txt);
        det_country=findViewById(R.id.det_country);
        det_yr=findViewById(R.id.det_year_runtime);
        det_direct=findViewById(R.id.det_director);
        det_poster=findViewById(R.id.det_poster);
        point=findViewById(R.id.det_point);
        ratingBar=findViewById(R.id.det_rate);
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(Detail.this,"امتیاز ثبت شد",Toasty.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }

    public class AA extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Detail.this);
            pd.setTitle("wait.");
            pd.setMessage("Get Data Server...");
            pd.setCancelable(true);
            pd.setIcon(R.drawable.ic_cloud);
            pd.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            getDetails();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
           // pd.dismiss();
        }
    }
}
