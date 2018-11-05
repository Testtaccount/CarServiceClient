package am.gsoft.carserviceclient.ui.adapter;

import static am.gsoft.carserviceclient.util.DateUtils.getDateFormat;
import static am.gsoft.carserviceclient.util.DateUtils.longToString;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.data.database.entity.Oil;
import am.gsoft.carserviceclient.ui.adapter.OilHistoryAdapter.OilHistoryViewHolder;
import am.gsoft.carserviceclient.util.DateUtils.DateType;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OilHistoryAdapter extends RecyclerView.Adapter<OilHistoryViewHolder> {

  private ArrayList<Oil> mOilArrayList;
  private OnOilHistoryItemClickListener itemClickListener;
//  private int counterNumber;
  boolean asc;

  public OilHistoryAdapter( OnOilHistoryItemClickListener itemClickListener) {
    this.mOilArrayList = new ArrayList<>();
    this.itemClickListener = itemClickListener;
//    counterNumber = mOilArrayList.size();
    asc=false;
  }

//  public void setGeofencePlaces(List<Oil> places) {
//
//    if (places == null || places.size() == 0) {
//      return;
//    }
//    if (places != null && places.size() > 0) {
//      this.mOilArrayList.clear();
//    }
//    this.mOilArrayList.addAll(places);
//    notifyDataSetChanged();
//
//  }
//
//  public void addGeofencePlace(Oil spinnerItem) {
//    this.mOilArrayList.add(spinnerItem);
//    notifyDataSetChanged();
//  }
//
//  public void deleteGeofencePlace(int placeId) {
//    for (Oil spinnerItem: mOilArrayList) {
//      if (placeId == spinnerItem.getId()) {
//        this.mOilArrayList.remove(spinnerItem);
//        notifyDataSetChanged();
//        return;
//      }
//    }
//  }

  public boolean isEmpty(){
    return mOilArrayList.size()==0;
  }

  @Override
  public OilHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Get the RecyclerView item layout
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_oil_history, parent, false);
    return new OilHistoryViewHolder(view, itemClickListener);
  }

  @Override
  public void onBindViewHolder(OilHistoryViewHolder holder, int position) {

    holder.bindData(position);

//    holder.itemView.setTag(mOilArrayList.getAppNotification(position).getPlaceId());

  }

  public void setOilList(List<Oil> oils) {
    if (oils == null ) {
      return;
    }else if(oils.size()==0){
      return;
    }

    if (mOilArrayList != null) {
      mOilArrayList.clear();
    }

    mOilArrayList.addAll(oils);
    notifyDataSetChanged();

  }


  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public int getItemCount() {
    if (mOilArrayList.isEmpty()) {
      return 0;
    }
    return mOilArrayList.size();
  }

//  public void setCounterNumber(int number) {
//    if(number!=1){
//      asc=false;
//    }else {
//      asc=true;
//    }
//    this.counterNumber = number;
//  }

  public void sort() {
    asc=!asc;
    Collections.reverse(mOilArrayList);
    notifyDataSetChanged();
  }

  /**
   * OilHistoryViewHolder class for the recycler view item
   */
  public class OilHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView rootRl;
    private TextView oilBrandItemTv;
    private TextView oilTypeItemTv;
    private TextView oilServiceDoneDateItemTv;
    private TextView oilVolumeItemTv;
    private TextView numberItemTv;

    public Oil oil;

    private OnOilHistoryItemClickListener itemClickListener;


    public OilHistoryViewHolder(View itemView, OnOilHistoryItemClickListener itemClickListener) {
      super(itemView);
      this.itemClickListener = itemClickListener;
      findViews(itemView);
    }

    void findViews(View view) {
      rootRl = (CardView) view.findViewById(R.id.item_oil_history_card_root);
      rootRl.setOnClickListener(this);
      oilBrandItemTv = (TextView) view.findViewById(R.id.tv_item_oil_brand);
      oilTypeItemTv = (TextView) view.findViewById(R.id.tv_item_oil_type);
      oilServiceDoneDateItemTv = (TextView) view.findViewById(R.id.tv_item_oil_service_done_date);
      oilVolumeItemTv = (TextView) view.findViewById(R.id.tv_item_oil_volume);
      numberItemTv = (TextView) view.findViewById(R.id.tv_item_number);
    }

    public void bindData(int position) {

      this.oil = mOilArrayList.get(position);

      oilBrandItemTv.setText(oilBrandItemTv != null ? String.valueOf(oil.getBrand()) : "");
      oilTypeItemTv.setText(oilTypeItemTv != null ? String.valueOf(oil.getType()) : "");
      oilServiceDoneDateItemTv.setText(oilTypeItemTv != null ? oil.getServiceDoneDate() == 0 ? "-"
          : longToString(oil.getServiceDoneDate(), getDateFormat(DateType.DMY)) : "");
      oilVolumeItemTv.setText(oilVolumeItemTv != null ? String.valueOf(oil.getVolume() + "L") : "");

      if (asc) {
        numberItemTv.setText(Integer.toString(position+1));
      } else {
        numberItemTv.setText(Integer.toString(mOilArrayList.size()-position));
      }
    }

    private void notifyPlaceItemClicked() {
      if (itemClickListener != null) {
        itemClickListener.onOilHistoryItemClick(oil);
      }
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.item_oil_history_card_root:
          notifyPlaceItemClicked();
          break;
      }
    }

  }

  public interface OnOilHistoryItemClickListener {

    void onOilHistoryItemClick(Oil oil);

  }

}
