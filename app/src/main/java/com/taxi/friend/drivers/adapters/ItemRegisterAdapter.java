package com.taxi.friend.drivers.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxi.friend.drivers.R;
import com.taxi.friend.drivers.models.ItemRegisterDriverModel;

import java.util.List;

public class ItemRegisterAdapter extends PagerAdapter {

    private List<ItemRegisterDriverModel> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageView[] imagesList;
    private ImageButton btnCamera;
    private ImageButton btnGallery;

    public ItemRegisterAdapter(List<ItemRegisterDriverModel> models, Context context) {
        this.models = models;
        this.context = context;
        imagesList = new ImageView[models.size()];
        for (int i=0; i < models.size(); i++) {
            imagesList[i] = null;
        }
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_driver_register, container, false);

        ImageView imageView;
        TextView title, description;

        title = view.findViewById(R.id.txtTittleDriverRegister);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);
        imageView = view.findViewById(R.id.imageCIFrontal);
        imageView.setImageResource(models.get(position).getImage());
        if(imagesList[position] != null)
        {
            Bitmap bitmap = ((BitmapDrawable)(imagesList[position].getDrawable())).getBitmap();
            imageView.setImageBitmap(bitmap);
        }

        title.setText(models.get(position).getTitle());

        container.addView(view);
        imagesList[position] = imageView;

        ImageButton.OnClickListener mTakePicSOnClickListener =
                new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                    }
                };

        setBtnListenerOrDisable(
                btnCamera,
                mTakePicSOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public ImageView getImageView(int position){
        if(position < imagesList.length){
            return imagesList[position];
        }

        return null;
    }
}
