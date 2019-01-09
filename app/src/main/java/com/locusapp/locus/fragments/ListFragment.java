package com.locusapp.locus.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.locusapp.locus.activities.app.CreateBountyActivity;
import com.locusapp.locus.R;
import com.locusapp.locus.adapters.ListAdapter;
import com.locusapp.locus.models.FirebaseDAO;
import com.mapbox.mapboxsdk.geometry.LatLng;


import java.util.ArrayList;

public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "ListFragment";

    private FloatingActionButton btnCreateBounty;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        FirebaseDAO firebaseDAO = new FirebaseDAO();

        firebaseDAO.getBountyList(new FirebaseDAO.FirebaseListCallback() {

            @Override
            public void onCallback(ArrayList<String> titles, ArrayList<LatLng> locations, ArrayList<String> ids) {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
                recyclerView.addItemDecoration(divider);
                ListAdapter adapter = new ListAdapter(ids, locations, titles, getContext());
                recyclerView.setAdapter(adapter);

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCreateBounty = getView().findViewById(R.id.btnCreateBounty);
        btnCreateBounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateBountyActivity.class);
                startActivity(intent);
            }
        });
        Log.d(TAG, "initRecyclerView: init");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
