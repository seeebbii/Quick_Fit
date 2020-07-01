package com.example.quickfit.Brands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickfit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class BrandsCustomAdapter extends BaseAdapter implements Filterable {

    private List<BrandItemsModel> brandModelList;
    private List<BrandItemsModel> brandModelListFiltered;
    private Context context;

    public BrandsCustomAdapter(List<BrandItemsModel> brandModelList, Context context) {
        this.brandModelList = brandModelList;
        this.brandModelListFiltered = brandModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return brandModelListFiltered.size();
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

        View rootView = inflater.inflate(R.layout.brands_row_model,null);
        ImageView brandImage = rootView.findViewById(R.id.brand_image);
        TextView brandName = rootView.findViewById(R.id.brand_name);
        brandImage.setImageResource(brandModelListFiltered.get(position).getBrandImage());
        brandName.setText(brandModelListFiltered.get(position).getBrandName());
        return rootView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence == null || charSequence.length() == 00){
                    filterResults.count = brandModelList.size();
                    filterResults.values = brandModelList;
                }else{
                    String searchStr = charSequence.toString().toLowerCase();
                    List<BrandItemsModel> resultData = new ArrayList<BrandItemsModel>();
                    for(BrandItemsModel brandsModel: brandModelList){
                        if(brandsModel.getBrandName().contains(searchStr)){
                            resultData.add(brandsModel);
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                brandModelListFiltered = (List<BrandItemsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
