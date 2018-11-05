package am.gsoft.carserviceclient.ui.adapter;

import static am.gsoft.carserviceclient.util.AppUtil.setTextViewDrawableColor;

import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarBrandsDialogAdapter extends ArrayAdapter<BrandsSpinnerItem> {

  private Context context;
  private int mSelectedIndex = -1;
  private int color;
  List<BrandsSpinnerItem> carBrandsList = null;
  private ArrayList<BrandsSpinnerItem> arraylist;
  Resources mResources;

  public CarBrandsDialogAdapter(Context context, List<BrandsSpinnerItem> carBrandsList,int color) {
    super(context, R.layout.spinner_brands_dropdown_item, carBrandsList);
    this.context = context;
    this.carBrandsList = carBrandsList;
    this.arraylist = new ArrayList<BrandsSpinnerItem>();
    this.arraylist.addAll(carBrandsList);
    this.color=color;
    this.mResources=context.getResources();

  }

  public void setSelection(int position) {
    mSelectedIndex =  position;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return carBrandsList.size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

//  @Override
//  public boolean isEnabled(int position) {
//    if (position == 0) {
//      // Disable the first item from Spinner
//      return false;
//    } else {
//      return true;
//    }
//  }

  //этот метод устанавливает выбранный элемент на spinner
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = convertView;
    if (row == null) {
      LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = layoutInflater.inflate(R.layout.spinner_brands_dropdown_item, parent, false);
    }
    if (row.getTag() == null) {
      BrandsSpinnerViewHolder brandsHolder = new BrandsSpinnerViewHolder();
      brandsHolder.setIcon((ImageView) row.findViewById(R.id.icon));
      brandsHolder.setCarBrand((TextView) row.findViewById(R.id.tv_item_car_brand));

      row.setTag(brandsHolder);
    }

//rellenamos el layout con los carBrandsList de la fila que se está procesando
    BrandsSpinnerItem spinnerItem = carBrandsList.get(position);
    ((BrandsSpinnerViewHolder) row.getTag()).getIcon().setImageDrawable(mResources.getDrawable(mResources.getIdentifier(spinnerItem.getIcon(), "drawable", "am.gsoft.carserviceclient")));
    ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setText(spinnerItem.getCarBrand());
    Typeface typeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/NotoSansArmenian-Regular.ttf");
    ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setTypeface(typeface);
    setTextViewDrawableColor(((BrandsSpinnerViewHolder) row.getTag()).getCarBrand(),android.R.color.transparent);

//    if (position == 0) {
//      // Set the disable item text color
//      ((BrandsSpinnerViewHolder) row.getTag()).getIcon().setVisibility(View.INVISIBLE);
//      ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setTextColor(Color.GRAY);
//    } else {
//      ((BrandsSpinnerViewHolder) row.getTag()).getIcon().setVisibility(View.VISIBLE);
//      ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setTextColor(Color.BLACK);
//    }

    if (position == mSelectedIndex) {
      row.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_background_E5E5E5));
    } else {
      row.setBackgroundColor(Color.TRANSPARENT);
    }
    return row;
  }

  //управляет списком с помощью шаблона Holder Pattern. Эквивалент типичной реализации getView
  //адаптера из обычного ListView

//  @Override
//  public View getDropDownView(int position, View convertView, ViewGroup parent) {
//    View row = convertView;
//    if (row == null) {
//      LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//      row = layoutInflater.inflate(R.layout.spinner_brands_dropdown_item, parent, false);
//    }
//
//    if (row.getTag() == null) {
//      BrandsSpinnerViewHolder brandsHolder = new BrandsSpinnerViewHolder();
//      brandsHolder.setIcon((ImageView) row.findViewById(R.id.icon));
//      brandsHolder.setCarBrand((TextView) row.findViewById(R.id.tv_item_car_brand));
//
//      row.setTag(brandsHolder);
//    }
//
//    //rellenamos el layout con los carBrandsList de la fila que se está procesando
//    BrandsSpinnerItem spinnerItem = carBrandsList.getAppNotification(position);
//    ((BrandsSpinnerViewHolder) row.getTag()).getIcon().setImageResource(spinnerItem.getIcon());
//    ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setText(spinnerItem.getCarBrand());
//    Typeface typeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/NotoSansArmenian-Regular.ttf");
//    ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setTypeface(typeface);
//    setTextViewDrawableColor(((BrandsSpinnerViewHolder) row.getTag()).getCarBrand(),android.R.color.transparent);
//
//    if (position == 0) {
//      // Set the disable item text color
//      ((BrandsSpinnerViewHolder) row.getTag()).getIcon().setVisibility(View.INVISIBLE);
//      ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setTextColor(Color.GRAY);
//    } else {
//      ((BrandsSpinnerViewHolder) row.getTag()).getIcon().setVisibility(View.VISIBLE);
//      ((BrandsSpinnerViewHolder) row.getTag()).getCarBrand().setTextColor(Color.BLACK);
//    }
//
//    if (position == mSelectedIndex) {
//      row.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_E5E5E5));
//    } else {
//      row.setBackgroundColor(Color.TRANSPARENT);
//    }
//
//    return row;
//  }

  public void filter(String charText) {
    charText = charText.toLowerCase(Locale.getDefault());
    carBrandsList.clear();
    if (charText.length() == 0) {
      carBrandsList.addAll(arraylist);
    } else {
      for (BrandsSpinnerItem item : arraylist) {
        if (item.getCarBrand().toLowerCase(Locale.getDefault()).contains(charText)) {
          carBrandsList.add(item);
        }
      }
    }
    notifyDataSetChanged();
  }



  /**
   * Holder para el Adapter del Spinner
   *
   * @author danielme.com
   */
  private static class BrandsSpinnerViewHolder {

    private ImageView icon;
    private TextView carBrand;


    public ImageView getIcon() {
      return icon;
    }

    public void setIcon(ImageView icon) {
      this.icon = icon;
    }

    public TextView getCarBrand() {
      return carBrand;
    }

    public void setCarBrand(TextView carBrand) {
      this.carBrand = carBrand;
    }

  }
}


