package org.gdgpoland.devfest.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gdgpoland.devfest.R;
import org.gdgpoland.devfest.objects.Conference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Adapter for the {@link org.gdgpoland.devfest.MainActivity},
 * display either a Conference slot or a break.
 * @author Arnaud Camus
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MainAdapter.class.getSimpleName();
    public static final int VIEW_HEADER = 0;
    public static final int VIEW_CONFERENCE = 1;

    private List<Conference> mData;
    private Context mContext;
    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormat2;

    public MainAdapter(Context context, List<Conference> objects) {
        mData = objects;
        mContext = context.getApplicationContext();
        simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        simpleDateFormat2 = new SimpleDateFormat(" - HH:mm", Locale.ENGLISH);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).getSpeaker() != null) {
            if(TextUtils.isEmpty(mData.get(position).getSpeaker())) {
                return VIEW_HEADER;
            }
        }
        return VIEW_CONFERENCE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (viewType == VIEW_HEADER) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_header, parent, false);
            return new org.gdgpoland.devfest.adapters.ViewHolderHeader(v);
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_conference, parent, false);
        return new org.gdgpoland.devfest.adapters.ViewHolderConference(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof org.gdgpoland.devfest.adapters.ViewHolderHeader) {
            try {
                ((ViewHolderHeader)holder).dateStart.setText(
                        new StringBuilder()
                                .append(simpleDateFormat.format(new Date(mData.get(position).getStartDate())))
                                .append(simpleDateFormat2.format(new Date(mData.get(position).getEndDate())))
                                .toString());
            } catch (Exception e) {
                Log.e(TAG, "onBindViewHolder, problem with parsing date: " + mData.get(position).getStartDate() + ", or " + mData.get(position).getEndDate());
                ((ViewHolderHeader)holder).dateStart.setText("");
            }
            ((org.gdgpoland.devfest.adapters.ViewHolderHeader)holder).headline.setText(mData.get(position).getHeadeline());

            if (mData.get(position).getHeadeline().equals(mContext.getString(R.string.coffee_break))) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.schedule_coffee));
                ((org.gdgpoland.devfest.adapters.ViewHolderHeader)holder).iconHeader.setImageResource(R.drawable.ic_local_cafe_white_36dp);
            } else if (mData.get(position).getHeadeline().equals(mContext.getString(R.string.lunch))) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.schedule_lunch));
                ((org.gdgpoland.devfest.adapters.ViewHolderHeader)holder).iconHeader.setImageResource(R.drawable.ic_restaurant_menu_white_36dp);
            } else if (mData.get(position).getHeadeline().equals(mContext.getString(R.string.tba))) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.windowBackground));
                ((org.gdgpoland.devfest.adapters.ViewHolderHeader)holder).iconHeader.setImageDrawable(null);
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.schedule_announcment));
                ((org.gdgpoland.devfest.adapters.ViewHolderHeader)holder).iconHeader.setImageResource(R.drawable.ic_people_white_36dp);
            }

        } else if (holder instanceof org.gdgpoland.devfest.adapters.ViewHolderConference) {
            try {
                ((ViewHolderConference)holder).dateStart.setText(
                        new StringBuilder()
                                .append(simpleDateFormat.format(new Date(mData.get(position).getStartDate())))
                                .append(simpleDateFormat2.format(new Date(mData.get(position).getEndDate())))
                                .toString());
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "onBindViewHolder, problem with parsing date: " + mData.get(position).getStartDate() + ", or " + mData.get(position).getEndDate());
                ((ViewHolderConference)holder).dateStart.setText("");
            }

            ((org.gdgpoland.devfest.adapters.ViewHolderConference)holder).location.setText(mData.get(position).getLocation());
            ((org.gdgpoland.devfest.adapters.ViewHolderConference)holder).headline.setText(mData.get(position).getHeadeline());

            if (mData.get(position).isFavorite(mContext)) {
                ((org.gdgpoland.devfest.adapters.ViewHolderConference) holder).favorite.setVisibility(View.VISIBLE);
                ((org.gdgpoland.devfest.adapters.ViewHolderConference)holder).favorite
                        .setImageResource(R.drawable.ic_favorite_grey600_18dp);
            } else {
                ((org.gdgpoland.devfest.adapters.ViewHolderConference) holder).favorite.setVisibility(View.GONE);
            }
        }
    }
}