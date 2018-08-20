package am.gsoft.carserviceclient.ui.adapter;


import java.io.Serializable;

public class BrandsSpinnerItem implements Serializable {

  private static final long serialVersionUID = 1229264567108962069L;
  private int icon;
  private String carBrand;

  public BrandsSpinnerItem() {
  }

  public BrandsSpinnerItem(int icon, String carBrand) {
    this.icon = icon;
    this.carBrand = carBrand;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public String getCarBrand() {
    return carBrand;
  }

  public void setCarBrand(String carBrand) {
    this.carBrand = carBrand;
  }
}
