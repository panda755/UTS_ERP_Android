package com.example.leadapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.leadapp.model.Lead;

import java.util.ArrayList;
import java.util.List;

public class LeadAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<Lead> items = new ArrayList<>();

    public LeadAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<Lead> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Lead getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) {
        Integer id = items.get(position).getId();
        return id != null ? id : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lead, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvLeadName);
            holder.tvCompany = convertView.findViewById(R.id.tvLeadCompany);
            holder.tvStatus = convertView.findViewById(R.id.tvLeadStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Lead lead = items.get(position);
        holder.tvName.setText(lead.getName());
        String company = lead.getCompanyName();
        holder.tvCompany.setText(company == null || company.isEmpty() ? "-" : company);
        holder.tvStatus.setText("Status: " + lead.getStatus()
                + "  |  Terakhir dihubungi: " + lead.getLastContactedDate());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvCompany;
        TextView tvStatus;
    }
}