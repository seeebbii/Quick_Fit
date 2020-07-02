package com.example.quickfit.Services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickfit.Brands.BrandItemsModel;
import com.example.quickfit.R;

import java.util.ArrayList;
import java.util.List;

public class ServicesCustomAdapter extends BaseAdapter implements Filterable {

    private List<ServicesItemModel> serviceModelList;
    public static List<ServicesItemModel> serviceModelListFiltered;
    private Context context;

    public ServicesCustomAdapter(List<ServicesItemModel> serviceModelList, Context context) {
        this.serviceModelList = serviceModelList;
        this.serviceModelListFiltered = serviceModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return serviceModelListFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.services_row_model,null);
        ImageView serviceImage = rootView.findViewById(R.id.service_image);
        TextView serviceName = rootView.findViewById(R.id.service_name);
        serviceImage.setImageResource(serviceModelListFiltered.get(position).getServiceImage());
        serviceName.setText(serviceModelListFiltered.get(position).getServiceName());

        return rootView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence == null || charSequence.length() == 0){
                    filterResults.count = serviceModelList.size();
                    filterResults.values = serviceModelList;
                }else{
                    String searchStr = charSequence.toString().toLowerCase();
                    List<ServicesItemModel> resultData = new ArrayList<ServicesItemModel>();
                    for(ServicesItemModel serviceModel: serviceModelList){
                        if(serviceModel.getServiceName().contains(searchStr)){
                            resultData.add(serviceModel);
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                serviceModelListFiltered = (List<ServicesItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}

