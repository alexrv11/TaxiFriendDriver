package com.taxi.friend.drivers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.amazonaws.taxifriend.orders.ListOrdersQuery;
import com.taxi.friend.drivers.constants.Constants;

import com.taxi.friend.drivers.models.Order;
import com.taxi.friend.drivers.models.OrderStatus;
import com.taxi.friend.drivers.services.OrderService;
import com.taxi.friend.drivers.utils.GPSCoordinate;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderDialogFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    private ListOrdersQuery.Item item;
    private android.location.Location taxiLocation;
    private final  double MINUTE_RATE = 0.001;

    public OrderDialogFragment(ListOrdersQuery.Item item, android.location.Location location) {
        this.item = item;
        this.taxiLocation = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.order_item, container, false);

        double distance = GPSCoordinate.distanceInMeterBetweenEarthCoordinates(item.latitude(),
                item.longitude(), taxiLocation.getLatitude(), taxiLocation.getLongitude());

        TextView messageView = view.findViewById(R.id.message);

        int totalDistanceInMinutes = (int)(distance * MINUTE_RATE);




        if(totalDistanceInMinutes > 0) {
            messageView.setText("Tienes un cliente a " + totalDistanceInMinutes + " min");
        } else {
            messageView.setText("Tienes un cliente cerca de ti");
        }

        Button acceptButton = view.findViewById(R.id.btnAccept);
        Button rejectButton = view.findViewById(R.id.btnCancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.id();
                String status = Constants.ORDER_STATUS_ACCEPTED;
                updateOrder(id, status);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.id();
                String status = Constants.ORDER_STATUS_REJECTED;
                updateOrder(id, status);

            }
        });


        return view;
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void updateOrder(String id, String status) {
        final Context context = this.getContext();
        try {
            OrderService service = new OrderService();
            OrderStatus orderStatus = new OrderStatus(status);
            Call<Order> orderCall = service.updateOrderStatus(id, orderStatus);
            orderCall.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, retrofit2.Response<Order> response) {
                    Order order = response.body();
                    if (response.code() != HttpURLConnection.HTTP_OK) {
                        return;
                    }
                    finalizeDialog();
                    Toast.makeText(context, "order status:" + order.getStatus(), Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                    finalizeDialog();

                }
            });
        }catch (Exception e) {
            Log.e("ErrorUpdateOrder", e.getMessage());
        }
    }

    private void finalizeDialog() {
        TaxiGlobalInfo.isShowOrderDialog = false;
        this.dismiss();
    }
}