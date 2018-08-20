package am.gsoft.carserviceclient.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MultiTextWatcher {
  private TextWatcherWithInstance callback;

  public MultiTextWatcher setCallback(TextWatcherWithInstance callback) {
    this.callback = callback;
    return this;
  }

  public MultiTextWatcher registerEditText(final EditText editText) {
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        callback.beforeTextChanged(editText, s, start, count, after);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        callback.onTextChanged(editText, s, start, before, count);
      }

      @Override
      public void afterTextChanged(Editable editable) {
        callback.afterTextChanged(editText, editable);
      }
    });

    return this;
  }

  public interface TextWatcherWithInstance {
    void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after);

    void onTextChanged(EditText editText, CharSequence s, int start, int before, int count);

    void afterTextChanged(EditText editText, Editable editable);
  }
}

/*
*
  private class GenericTextWatcher implements TextWatcher {

    boolean _ignore = false; // indicates if the change was made by the TextWatcher itself.
    private View view;

    private GenericTextWatcher(View view) {
      this.view = view;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    public void afterTextChanged(Editable s) {
      String text = s.toString();
      if (_ignore) {
        return;
      }

      _ignore = true; // prevent infinite loop

//       Change your text here.
//       myTextView.setText(myNewText);

      switch (view.getId()) {
        case R.id.et_service_done_km:

//          serviceDoneKmEt.setText(text);
          break;
        case R.id.et_recomended_km:

//          recomendedKmEt.setText(text);

//          nextServiceKmEt.setText(String .valueOf(Integer.valueOf(serviceDoneKmEt.getText().toString())+Integer.valueOf(text)));

          break;
        case R.id.et_next_service_km:

//          nextServiceKmEt.setText(text);

//          recomendedKmEt.setText(String .valueOf(Integer.valueOf(text)-Integer.valueOf(serviceDoneKmEt.getText().toString())));

          break;

      }

      _ignore = false; // release, so the TextWatcher start to listen again.
    }
  }
*
* */