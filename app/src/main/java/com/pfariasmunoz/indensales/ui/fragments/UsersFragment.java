package com.pfariasmunoz.indensales.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FbContract.UserEntry;
import com.pfariasmunoz.indensales.data.models.IndenUser;
import com.pfariasmunoz.indensales.ui.AdapterSetter;
import com.pfariasmunoz.indensales.ui.activities.BaseActivity;
import com.pfariasmunoz.indensales.ui.activities.EditUserActivity;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.ui.viewholders.UserViewHolder;
import com.pfariasmunoz.indensales.utils.MathHelper;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends BaseFragment implements AdapterSetter{

    private Query mQuery;
    private FirebaseRecyclerAdapter<IndenUser, UserViewHolder> mAdapter;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQuery = UserEntry.sRef;
        setupAdapter(mQuery);
    }

    @Override
    public void setupAdapter(Query query) {
        mAdapter = new FirebaseRecyclerAdapter<IndenUser, UserViewHolder>(
                IndenUser.class,
                R.layout.item_user,
                UserViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, IndenUser model, final int position) {
                if (model != null) {
                    viewHolder.bind(model);
                    updateViews();

                    viewHolder.getPopupMenuButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PopupMenu popupMenu = new PopupMenu(viewHolder.itemView.getContext(), view);
                            final MenuInflater inflater = popupMenu.getMenuInflater();
                            inflater.inflate(R.menu.user_popup, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_popup_edit_user:
                                            Intent intent = new Intent(getActivity(), EditUserActivity.class);
                                            intent.putExtra("user_id", getRef(position).getKey());
                                            startActivity(intent);
                                            return true;
                                        case R.id.menu_popup_add_or_remove_clients:
                                            String userUid = getRef(position).getKey();
                                            ((MainActivity)getContext()).addClientsToUser(userUid);
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();
                        }
                    });

                    viewHolder.getAddClientsToUserButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String userUid = getRef(position).getKey();
                            ((MainActivity)getContext()).addClientsToUser(userUid);
                        }
                    });

                    viewHolder.getEditUserButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), EditUserActivity.class);
                            intent.putExtra("user_id", getRef(position).getKey());
                            startActivity(intent);
                        }
                    });
                }
                ((BaseActivity)getContext()).writeNewUserIfNeeded();
            }


            @Override
            protected void onDataChanged() {
                updateViews();
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(getActivity().getResources().getString(R.string.search_clients_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!TextUtils.isEmpty(newText)) {
                        if (MathHelper.isNumeric(newText)) {
                            setupAdapter(mQuery);
                        } else {
                            String name = newText.toUpperCase();
                            Query nameQuery = UserEntry.getUserByName(name);
                            setupAdapter(nameQuery);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);

                    } else {
                        mQuery = UserEntry.sRef;
                        setupAdapter(mQuery);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);
                    }
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
