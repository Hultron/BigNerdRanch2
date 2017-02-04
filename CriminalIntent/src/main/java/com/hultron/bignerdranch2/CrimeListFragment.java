package com.hultron.bignerdranch2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class CrimeListFragment extends Fragment {
  private RecyclerView mRecyclerView;
  private CrimeAdapter mAdapter;
  private int mItemUpdatedPosition;
  private boolean mSubtitleVisible;
  private static final String ARG_SUBTITLE_VISIBLE = "arg_subtitle_visible";



  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    /*从argument中获取subtitle visible*/
    mSubtitleVisible = getArguments().getBoolean(ARG_SUBTITLE_VISIBLE);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateUI();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    updateUI();
  }

  public static CrimeListFragment newInstance(boolean subtitleVisible) {
    Bundle args = new Bundle();
    args.putSerializable(ARG_SUBTITLE_VISIBLE, subtitleVisible);
    CrimeListFragment fragment = new CrimeListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private void updateUI() {
    CrimeLab crimeLab = CrimeLab.get(getActivity());
    List<Crime> crimes = crimeLab.getCrimes();

    if (mAdapter == null) {
      mAdapter = new CrimeAdapter(crimes);
      mRecyclerView.setAdapter(mAdapter);
    } else {
      mAdapter.notifyItemChanged(mItemUpdatedPosition);
      mItemUpdatedPosition = RecyclerView.NO_POSITION;
    }
    updateSubtitle();
  }
  private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Crime mCrime;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private CheckBox mSolvedCheckbox;

    public CrimeHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      mTitleTextView = (TextView)
          itemView.findViewById(R.id.list_item_crime_title_text_view);
      mDateTextView = (TextView)
          itemView.findViewById(R.id.list_item_crime_date_text_view);
      mSolvedCheckbox = (CheckBox)
          itemView.findViewById(R.id.list_item_crime_solved_checkbox);
    }

    public void bindCrime(Crime crime) {
      mCrime = crime;
      mTitleTextView.setText(crime.getTitle());
      mDateTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy",mCrime.getDate()));
      mSolvedCheckbox.setChecked(crime.isSolved());
    }

    @Override
    public void onClick(View v) {
      mItemUpdatedPosition = getAdapterPosition();
      Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId(),mSubtitleVisible);
      startActivity(intent);
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_crime_list, menu);
    MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
    if (mSubtitleVisible) {
      subtitleItem.setTitle(R.string.hide_subtitle);
    } else {
      subtitleItem.setTitle(R.string.show_subtitle);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_new_crime:
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity
            .newIntent(getActivity(),crime.getId(),mSubtitleVisible);
        startActivity(intent);
        return true;//结束该回调
      case R.id.menu_item_show_subtitle:
        mSubtitleVisible = !mSubtitleVisible;
        getActivity().invalidateOptionsMenu();
        updateSubtitle();
        return true;//结束该回调
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  private void updateSubtitle() {
    CrimeLab crimeLab = CrimeLab.get(getActivity());
    int crimeCount = crimeLab.getCrimes().size();
    String subtitle = getString(R.string.subtitle_format,crimeCount);

    if (!mSubtitleVisible) {
      subtitle = null;
    }

    AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.getSupportActionBar().setSubtitle(subtitle);
  }

  private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

    private List<Crime> mCrimes;

    private CrimeAdapter(List<Crime> crimes) {
      mCrimes = crimes;
    }

    @Override
    public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(getActivity())
          .inflate(R.layout.list_item_crime,parent,false);
      return new CrimeHolder(view);
    }

    @Override public void onBindViewHolder(CrimeHolder holder, int position) {
      Crime crime = mCrimes.get(position);
      holder.bindCrime(crime);
    }

    @Override public int getItemCount() {
      return mCrimes.size();
    }
  }

}
