package com.example.dontforgettograbthat.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.dontforgettograbthat.ActivityCart.CartActivity;
import com.example.dontforgettograbthat.Interface.CartInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.NotNull;

public class CartListDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "CartListDialog";
    CartInterface mInterface;

    private Button btnDeleteFromCartList, btnAddToHistory;
    private EditText etItemName, etListName, etPrice, etQuanity;
    private Item item;
    private int position;
    private Context mContext;
    private Button btnSetChanges;

    public static CartListDialog newInstance(Item item, int position) {
        CartListDialog frag = new CartListDialog();
        frag.setItems(item, position);
        return frag;
    }

    public void setItems(Item item, int position) {
        this.item=item;
        this.position=position;
    }
    @Override
    public void onAttach(Context context) {

        this.mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cart_item, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        referenceWidgets(view);
        mInterface = (CartActivity) getContext();


        if (item != null) {
            item = NotNull.item(item);
            Log.d(TAG, "onCreateView: non Null item = " + item.toString());

            etListName.setText(item.getList_name());
            etItemName.setText(item.getItem_name());
            String stPrice = item.getPrice().toString();
            etPrice.setText(stPrice);
            String stQuantity = "" + item.getQuantity();
            etQuanity.setText(stQuantity);
        }
        return view;
    }

    public void referenceWidgets(View view){

        btnDeleteFromCartList = view.findViewById(R.id.btnDeleteFromList);
        btnAddToHistory = view.findViewById(R.id.btnBoughtToHistory);
        btnSetChanges = view.findViewById(R.id.btnSetChanges);

        etPrice = view.findViewById(R.id.etItemPrice);
        etItemName = view.findViewById(R.id.etItemName);
        etListName = view.findViewById(R.id.etListName);
        etQuanity = view.findViewById(R.id.etQuantity);

        btnAddToHistory.setOnClickListener(this);
        btnDeleteFromCartList.setOnClickListener(this);
        btnSetChanges.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnDeleteFromList:
                mInterface.delete(item.getItemKey(), position);
                dismiss();
                break;

            case R.id.btnBoughtToHistory:
                mInterface.addToHistory(item, position);
                dismiss();
                break;

            case R.id.btnSetChanges:

                mInterface.setChanges(getCurrentItem(), position);

                dismiss();
        }

    }

    private Item getCurrentItem() {
        Item item = new Item();
        if (this.item!=null) {
            item.setItemKey(this.item.getItemKey());
        }
        item.setItem_name(etItemName.getText().toString());
        item.setList_name(etListName.getText().toString());
        item.setQuantity(Integer.parseInt(etQuanity.getText().toString()));
        item.setPrice(Double.parseDouble(etPrice.getText().toString()));

        return item;

    }
}
