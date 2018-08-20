package am.gsoft.carserviceclient.ui.adapter;

import static am.gsoft.carserviceclient.util.AppUtil.setTextViewDrawableColor;

import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.ui.adapter.BrandsAdapter.BrandsViewHolder;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsViewHolder> implements Filterable {

  private ArrayList<BrandsSpinnerItem> carBrandsList;
  private ArrayList<BrandsSpinnerItem> filterList;
  private OnBrandItemClickListener itemClickListener;
  private CustomFilter filter;


  public BrandsAdapter(ArrayList<BrandsSpinnerItem> carBrandsList, OnBrandItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
    this.carBrandsList = carBrandsList;
    this.filterList = carBrandsList;
  }

  public boolean isEmpty(){
    return carBrandsList.size()==0;
  }

  @Override
  public BrandsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Get the RecyclerView item layout
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.spinner_brands_dropdown_item, parent, false);
    return new BrandsViewHolder(view, itemClickListener);
  }

  @Override
  public void onBindViewHolder(BrandsViewHolder holder, int position) {

    Log.d("testt","position"+position);
    holder.bindData(carBrandsList.get(position));

  }


  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public int getItemCount() {
    if (carBrandsList.isEmpty()) {
      return 0;
    }
    return carBrandsList.size();
  }

//  public void filter(String charText) {
//    charText = charText.toLowerCase(Locale.getDefault());
//    this.carBrandsList.clear();
//    if (charText.length() == 0) {
//      this.carBrandsList.addAll(filterList);
//    } else {
//      for (BrandsSpinnerItem item : filterList) {
//        if (item.getCarBrand().toLowerCase(Locale.getDefault()).contains(charText)) {
//          this.carBrandsList.add(item);
//        }
//      }
//    }
//    notifyDataSetChanged();
//  }

  //RETURN FILTER OBJ
  @Override
  public Filter getFilter() {
    if(filter==null)
    {
      filter=new CustomFilter(filterList,this);
    }

    return filter;
  }

  /**
   * BrandsViewHolder class for the recycler view item
   */

  public class CustomFilter extends Filter{

    BrandsAdapter adapter;
    ArrayList<BrandsSpinnerItem> filterList;

    public CustomFilter(ArrayList<BrandsSpinnerItem> filterList,BrandsAdapter adapter)
    {
      this.adapter=adapter;
      this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults results=new FilterResults();

      //CHECK CONSTRAINT VALIDITY
      if(constraint != null && constraint.length() > 0){
        //CHANGE TO UPPER
        constraint=constraint.toString().toUpperCase();
        //STORE OUR FILTERED PLAYERS
        ArrayList<BrandsSpinnerItem> filterSpinnerItems = new ArrayList<>();

        for (int i=0;i<filterList.size();i++) {
          //CHECK
          if(filterList.get(i).getCarBrand().toUpperCase().contains(constraint)){
            //ADD PLAYER TO FILTERED PLAYERS
            filterSpinnerItems.add(filterList.get(i));
          }
        }

        results.count=filterSpinnerItems.size();
        results.values=filterSpinnerItems;
      }else{
        results.count=filterList.size();
        results.values=filterList;

      }

      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

      adapter.carBrandsList= (ArrayList<BrandsSpinnerItem>) results.values;

      //REFRESH
      adapter.notifyDataSetChanged();
    }
  }

  public class BrandsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LinearLayout rootRl;
    private ImageView icon;
    private TextView carBrand;

    public BrandsSpinnerItem spinnerItem;

    private OnBrandItemClickListener itemClickListener;


    public BrandsViewHolder(View itemView, OnBrandItemClickListener itemClickListener) {
      super(itemView);
      this.itemClickListener = itemClickListener;
      findViews(itemView);
    }

    void findViews(View view) {
      rootRl = (LinearLayout) view.findViewById(R.id.item_brand_root);
      rootRl.setOnClickListener(this);
      icon = (ImageView) view.findViewById(R.id.icon);
      carBrand = (TextView) view.findViewById(R.id.tv_item_car_brand);
    }

    public void bindData(BrandsSpinnerItem spinnerItem) {

      this.spinnerItem = spinnerItem;
      icon.setImageResource(spinnerItem.getIcon());
      carBrand.setText(spinnerItem.getCarBrand());
      Typeface typeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/NotoSansArmenian-Regular.ttf");
      carBrand.setTypeface(typeface);
      setTextViewDrawableColor(carBrand,android.R.color.transparent);

    }

    private void notifyItemClicked() {
      if (itemClickListener != null) {
        itemClickListener.onBrandItemClick(spinnerItem);
      }
    }



    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.item_brand_root:
          notifyItemClicked();
          break;
      }
    }

  }

  public interface OnBrandItemClickListener {

    void onBrandItemClick(BrandsSpinnerItem brandsSpinnerItem);

  }

}
