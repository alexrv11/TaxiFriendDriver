package com.taxi.friend.drivers;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.taxi.friend.drivers.adapters.ItemRegisterAdapter;
import com.taxi.friend.drivers.models.ItemRegisterDriverModel;

import java.util.ArrayList;
import java.util.List;

public class TaxiDriverRegisterActivity extends AppCompatActivity {

    private List<ItemRegisterDriverModel> models;
    private ItemRegisterAdapter adapter;
    private ViewPager viewPager;
    private Integer[] colours;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_driver_register);

        models = new ArrayList<>();
        models.add(new ItemRegisterDriverModel(R.drawable.ic_round_account_driver, "Foto Frontal Licencia", "Necesitamos validar tu identidad"));
        models.add(new ItemRegisterDriverModel(R.drawable.ic_round_card_identify, "Foto Anverso Licencia", "Validamos tus datos con SEGIP"));
        models.add(new ItemRegisterDriverModel(R.drawable.ic_round_directions_car, "Foto frontal Automovil", "Es importante visivilizar tu automovil para los clientes"));
        models.add(new ItemRegisterDriverModel(R.drawable.ic_round_directions_car, "Foto Vagoneta", "Algunos pasajeros necesitan llevar elemento, esta foto les permitira saber tienes espacio"));

        adapter = new ItemRegisterAdapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] coloTemp = {
                getResources().getColor(R.color.registerColor1),
                getResources().getColor(R.color.registerColor2),
                getResources().getColor(R.color.registerColor4)
        };

        colours = coloTemp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel) {
                if (position < (adapter.getCount() - 1) && position < (colours.length - 1)) {
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colours[position], colours[position + 1]));
                    return;
                }

                viewPager.setBackgroundColor(colours[colours.length - 1]);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);

        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
        super.onSaveInstanceState(outState);*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        /*super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.setVisibility(
                savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );*/
    }

}
