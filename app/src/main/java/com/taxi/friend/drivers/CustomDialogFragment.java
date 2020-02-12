package com.taxi.friend.drivers;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.amazonaws.taxifriend.orders.ListOrdersQuery;
import com.taxi.friend.drivers.models.Location;
import com.taxi.friend.drivers.utils.GPSCoordinate;

public class CustomDialogFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    private ListOrdersQuery.Item item;
    private android.location.Location taxiLocation;
    private final  double MINUTE_RATE = 0.001;

    public CustomDialogFragment(ListOrdersQuery.Item item, android.location.Location location) {
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
}