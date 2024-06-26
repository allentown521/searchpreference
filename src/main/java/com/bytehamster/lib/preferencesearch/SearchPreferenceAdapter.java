package com.bytehamster.lib.preferencesearch;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

class SearchPreferenceAdapter extends RecyclerView.Adapter<SearchPreferenceAdapter.ViewHolder> {
    private List<ListItem> dataset;
    private SearchConfiguration searchConfiguration;
    private SearchClickListener onItemClickListener;


    SearchPreferenceAdapter() {
        dataset = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == PreferenceItem.TYPE) {
            return new PreferenceViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.searchpreference_list_item_result, parent, false));
        } else {
            return new HistoryViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            allen.town.focus_common.R.layout.single_assist_chip, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder h, final int position) {
        final ListItem listItem = dataset.get(position);
        if (getItemViewType(position) == HistoryItem.TYPE) {
            HistoryViewHolder holder = (HistoryViewHolder) h;
            HistoryItem item = (HistoryItem) listItem;
            holder.chip.setText(item.getTerm());
        } else if (getItemViewType(position) == PreferenceItem.TYPE) {
            PreferenceViewHolder holder = (PreferenceViewHolder) h;
            PreferenceItem item = (PreferenceItem) listItem;
            holder.title.setText(item.title);

            if (TextUtils.isEmpty(item.summary)) {
                holder.summary.setVisibility(View.GONE);
            } else {
                holder.summary.setVisibility(View.VISIBLE);
                holder.summary.setText(item.summary);
            }

            if (searchConfiguration.isBreadcrumbsEnabled()) {
                holder.breadcrumbs.setText(item.breadcrumbs);
                holder.breadcrumbs.setAlpha(0.6f);
                holder.summary.setAlpha(1.0f);
            } else {
                holder.breadcrumbs.setVisibility(View.GONE);
                holder.summary.setAlpha(0.6f);
            }

        }

        h.root.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClicked(listItem, h.getAdapterPosition());
            }
        });
    }

    void setContent(List<ListItem> items) {
        dataset = new ArrayList<>(items);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataset.get(position).getType();
    }

    void setSearchConfiguration(SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    void setOnItemClickListener(SearchClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface SearchClickListener {
        void onItemClicked(ListItem item, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        ViewHolder(View v) {
            super(v);
            root = v;
        }
    }

    static class HistoryViewHolder extends ViewHolder {
        Chip chip;
        HistoryViewHolder(View v) {
            super(v);
            chip = v.findViewById(allen.town.focus_common.R.id.chip);
        }
    }

    static class PreferenceViewHolder extends ViewHolder {
        TextView title;
        TextView summary;
        TextView breadcrumbs;
        PreferenceViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            summary = v.findViewById(R.id.summary);
            breadcrumbs = v.findViewById(R.id.breadcrumbs);
        }
    }
}
