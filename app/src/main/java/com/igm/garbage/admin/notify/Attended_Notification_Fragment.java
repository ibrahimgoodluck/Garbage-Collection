package com.igm.garbage.admin.notify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.igm.garbage.MyAdapter;
import com.igm.garbage.R;
import com.igm.garbage.models.Subscription;

import java.util.ArrayList;
import java.util.List;


public class Attended_Notification_Fragment extends Fragment implements MyAdapter.OnNotificationListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
//    List<Request> list = new ArrayList<>();
    List<Subscription> list = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @NonNull Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attended__notification_, container, false);
        // Inflate the layout for this fragment

        recyclerView = view.findViewById(R.id.recycler_view_attended);
        recyclerView.setHasFixedSize(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new MyAdapter(list, this);
        recyclerView.setAdapter(mAdapter);

        FirebaseFirestore.getInstance()
                .collection("subscription")
                .get()
                .addOnSuccessListener( new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(  QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot document :queryDocumentSnapshots.getDocuments()) {

                            Log.d("Requests", "onSuccess: " + document.getData());

                            Subscription subscription = new Subscription(
                                    document.getId(),
                                    document.getString("userId"),
                                    document.getDate("startDate"),
                                    document.getDate("endDate"),
                                    document.getString("disabled"),
                                    document.getString("location"),
                                    document.getString("subscription"),
                                    document.getString("status"),
                                    document.getString("name"),
                                    document.getString("phone")
                            );

                            Log.d("TAG", "onSuccess: " + subscription.getUserId());

                            if (subscription.getStatus().equals("1"))
                                list.add(subscription);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });


        return view;
    }

    @Override
    public void onNotificationClick(int position) {
        //An alert for payments
        new AlertDialog.Builder(getActivity())
                .setTitle("Information")
                .setMessage("This request is already attended")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}