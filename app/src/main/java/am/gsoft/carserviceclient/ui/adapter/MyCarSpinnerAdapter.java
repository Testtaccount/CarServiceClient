package am.gsoft.carserviceclient.ui.adapter;

import am.gsoft.carserviceclient.R;
import am.gsoft.carserviceclient.app.App;
import am.gsoft.carserviceclient.data.database.entity.Car;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MyCarSpinnerAdapter extends ArrayAdapter<Car> {

  private Context context;
  private int mSelectedIndex = -1;
  List<Car> carList = new ArrayList<>();

  public MyCarSpinnerAdapter(Context context, List<Car> carList) {
    super(context, R.layout.spinner_selected_item, carList);
    this.context = context;
    this.carList = carList;
  }

  public void setSelection(int position) {
    mSelectedIndex =  position;
    notifyDataSetChanged();
  }

  //этот метод устанавливает выбранный элемент на spinner
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
          .inflate(R.layout.spinner_selected_item, null);
    }
    Car carForPosition=carList.get(position);
    ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(ContextCompat.getDrawable(App.getInstance(), carForPosition.getIcon()));//setBackgroundResource(carBrandsList.getAppNotification(position).getIcon());
    Typeface typeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/NotoSansArmenian-Regular.ttf");
    ((TextView) convertView.findViewById(R.id.tv_car_brand)).setText(carForPosition.getCarBrand());
    ((TextView) convertView.findViewById(R.id.tv_model)).setText(carForPosition.getModel());
    ((TextView) convertView.findViewById(R.id.tv_model)).setTypeface(typeface);
    ((TextView) convertView.findViewById(R.id.tv_numbers)).setText(carForPosition.getNumbers());
    Typeface typeface1 = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/gl-nummernschild-eng-webfont.ttf");
//    Typeface typeface = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/NotoSansArmenian-Regular.ttf");
    ((TextView) convertView.findViewById(R.id.tv_numbers)).setTypeface(typeface1);
//    ((TextView) convertView.findViewById(R.id.tv_year)).setText(carForPosition.getYear());

    return convertView;
  }

  //управляет списком с помощью шаблона Holder Pattern. Эквивалент типичной реализации getView
  //адаптера из обычного ListView

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    View row = convertView;
    if (row == null) {
      LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = layoutInflater.inflate(R.layout.spinner_list_item, parent, false);
    }

    if (row.getTag() == null) {
      MyCarViewHolder carViewHolder = new MyCarViewHolder();
      carViewHolder.setIcon((ImageView) row.findViewById(R.id.icon));
      carViewHolder.setCarBrand((TextView) row.findViewById(R.id.tv_car_brand));
      carViewHolder.setModel((TextView) row.findViewById(R.id.tv_model));
      carViewHolder.setNumbers((TextView) row.findViewById(R.id.tv_numbers));
//      carViewHolder.setYear((TextView) row.findViewById(R.id.tv_year));

      row.setTag(carViewHolder);
    }

    //rellenamos el layout con los carBrandsList de la fila que se está procesando
    Car car = carList.get(position);
    ((MyCarViewHolder) row.getTag()).getIcon().setImageResource(car.getIcon());
    ((MyCarViewHolder) row.getTag()).getCarBrand().setText(car.getCarBrand());
    ((MyCarViewHolder) row.getTag()).getModel().setText(car.getModel());
    ((MyCarViewHolder) row.getTag()).getNumbers().setText(car.getNumbers());
//    ((MyCarViewHolder) row.getTag()).getYear().setText(car.getYear());

    if (position == mSelectedIndex) {
      row.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_background_E5E5E5));
    } else {
      row.setBackgroundColor(Color.TRANSPARENT);
    }
    return row;
  }

  public void swapForecast(List<Car> cars) {
    // If there was no forecast data, then recreate all of the list
    if (carList == null) {
      carList = cars;
      notifyDataSetChanged();
    } else {

      carList.clear();
      carList = cars;
      notifyDataSetChanged();
    }
  }


  private static class MyCarViewHolder {

    private ImageView icon;
    private TextView carBrand;
    private TextView model;
    private TextView numbers;
    private TextView year;

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

    public TextView getModel() {
      return model;
    }

    public void setModel(TextView model) {
      this.model = model;
    }

    public TextView getNumbers() {
      return numbers;
    }

    public void setNumbers(TextView numbers) {
      this.numbers = numbers;
    }

    public TextView getYear() {
      return year;
    }

    public void setYear(TextView year) {
      this.year = year;
    }
  }
}


