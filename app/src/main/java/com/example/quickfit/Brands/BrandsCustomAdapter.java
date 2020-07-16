package com.example.quickfit.Brands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickfit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrandsCustomAdapter extends BaseAdapter implements Filterable {

    private List<BrandItemsModel> brandModelList;
    public static List<BrandItemsModel> brandModelListFiltered;
    private Context context;
    //ImageView brandImage ;
    TextView brandName ;
    String url ;



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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = null;
        if(view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.brands_row_model, null);
            viewHolder.brandImage = view.findViewById(R.id.brand_image);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
            brandName = view.findViewById(R.id.brand_name);
            url = brandModelListFiltered.get(position).getBrandImage();
            brandName.setText(brandModelListFiltered.get(position).getBrandName());

            Picasso.get()
                    .load("http://sania.co.uk/quick_fix/brands/"+url)
                    .into(viewHolder.brandImage);

        return view;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence == null || charSequence.length() == 0){
                    filterResults.count = brandModelList.size();
                    filterResults.values = brandModelList;
                }else{
                    String searchStr = charSequence.toString();
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
    private class ViewHolder {
        public ImageView brandImage;
    }
}
