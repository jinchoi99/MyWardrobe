package com.example.mywardrobe.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.activities.ComposeCategoryActivity;
import com.example.mywardrobe.activities.ComposeOutfitActivity;
import com.example.mywardrobe.adapters.ClothesAdapter;
import com.example.mywardrobe.adapters.OutfitsAdapter;
import com.example.mywardrobe.models.Category;
import com.example.mywardrobe.models.Clothing;
import com.example.mywardrobe.models.Outfit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OutfitsFragment extends Fragment {
    public static final String TAG = "OutfitsFragment";
    private RecyclerView rvOutfits;
    private OutfitsAdapter adapter;
    private List<Outfit> allOutfits;

    //pull-to-refresh
    private SwipeRefreshLayout swipeContainer;

    //Delete Outfit
    public static boolean deleteOutfitMode = false;
    private RelativeLayout rlPopUpDeleteOutfitDialog;
    private LinearLayout outfitsOverbox;
    private Button btnDeleteOutfitYes;
    private Button btnDeleteOutfitNo;
    private TextView tvDeleteOutfitMessage;
    Animation fromsmall;

    public OutfitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outfits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Delete Outfit
        fromsmall = AnimationUtils.loadAnimation(getContext(), R.anim.fromsmall);
        outfitsOverbox = view.findViewById(R.id.outfitsOverbox);
        outfitsOverbox.setAlpha(0);
        rlPopUpDeleteOutfitDialog = view.findViewById(R.id.rlPopUpDeleteOutfitDialog);
        btnDeleteOutfitYes = view.findViewById(R.id.btnDeleteOutfitYes);
        btnDeleteOutfitNo = view.findViewById(R.id.btnDeleteOutfitNo);
        tvDeleteOutfitMessage = view.findViewById(R.id.tvDeleteOutfitMessage);
        rlPopUpDeleteOutfitDialog.setVisibility(View.GONE);

        OutfitsAdapter.OnCheckDeleteClickListener onCheckDeleteClickListener = new OutfitsAdapter.OnCheckDeleteClickListener() {
            @Override
            public void onCheckDeleteClicked(final int position, final CheckBox cb) {
                rlPopUpDeleteOutfitDialog.setAlpha(1);
                rlPopUpDeleteOutfitDialog.setVisibility(View.VISIBLE);
                rlPopUpDeleteOutfitDialog.startAnimation(fromsmall);
                outfitsOverbox.animate().alpha(1.0f).setDuration(800);

                final Outfit currentOutfit = allOutfits.get(position);
                tvDeleteOutfitMessage.setText("Are you sure you want to delete \"" + currentOutfit.getOutfitName() + "\" ?");

                //Remove Outfit
                btnDeleteOutfitYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Remove clothing from Parse
                        try {
                            currentOutfit.delete();
                            Toast.makeText(view.getContext(), "successfully deleted outfit", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Remove from clothes list
                        allOutfits.remove(position);

                        cb.setChecked(false);
                        closeDeleteDialog();
                    }
                });

                //Cancel deletion
                btnDeleteOutfitNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cb.setChecked(false);
                        closeDeleteDialog();
                    }
                });
            }
        };

        rvOutfits = view.findViewById(R.id.rvOutfits);
        allOutfits = new ArrayList<>();
        adapter = new OutfitsAdapter(getContext(), allOutfits, onCheckDeleteClickListener);
        rvOutfits.setAdapter(adapter);
        rvOutfits.setLayoutManager(new LinearLayoutManager(getContext()));
        queryOutfits();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerOutfits);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                queryOutfits();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void closeDeleteDialog() {
        outfitsOverbox.animate().alpha(0.0f).setDuration(500);
        rlPopUpDeleteOutfitDialog.animate().alpha(0.0f).setDuration(500);
        rlPopUpDeleteOutfitDialog.setVisibility(View.GONE);
        deleteOutfitMode=false;
        adapter.notifyDataSetChanged();
    }

    private void queryOutfits() {
        ParseQuery<Outfit> query = ParseQuery.getQuery(Outfit.class);
        query.include(Outfit.KEY_OUTFIT_CLOTHES);
        query.setLimit(20);
        query.whereEqualTo(Outfit.KEY_OUTFIT_OWNER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Outfit.KEY_OUTFIT_CREATED_KEY);
        query.findInBackground(new FindCallback<Outfit>() {
            @Override
            public void done(List<Outfit> outfits, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting outfits",e);
                    return;
                }
                allOutfits.addAll(outfits);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_outfit, menu);
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        menu.getItem(1).setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.composeOutfit:
                Toast.makeText(getContext(), "compose new outfit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ComposeOutfitActivity.class);
                startActivity(intent);
                break;
            case R.id.deleteOutfit:
                deleteOutfitMode = true;
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}