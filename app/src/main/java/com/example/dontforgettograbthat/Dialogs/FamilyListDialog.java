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

import com.example.dontforgettograbthat.ActivityFamilyList.FamilyListActivity;
import com.example.dontforgettograbthat.Interface.FamilyListInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.NotNull;

public class FamilyListDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "FamilyListDialog";
    FamilyListInterface mInterface;

    private Button btnDeleteFromCartList, btnAcceptIntoCart;
    private EditText etItemName, etListName, etPrice, etQuanity;
    private Item item;
    private int position;
    private Context mContext;
    private Button btnSetChanges;

    public static FamilyListDialog newInstance(Item item, int position) {
        FamilyListDialog frag = new FamilyListDialog();
        frag.setItems(item, position);
        return frag;
    }

    public void setItems(Item item, int position) {
        this.item=item;
        this.position=position;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_family_item, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        referenceWidgets(view);
        mInterface = (FamilyListActivity) getContext();


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
        btnAcceptIntoCart = view.findViewById(R.id.btnAcceptIntoCart);
        btnSetChanges = view.findViewById(R.id.btnSetChanges);

        etPrice = view.findViewById(R.id.etItemPrice);
        etItemName = view.findViewById(R.id.etItemName);
        etListName = view.findViewById(R.id.etListName);
        etQuanity = view.findViewById(R.id.etQuantity);

        btnAcceptIntoCart.setOnClickListener(this);
        btnDeleteFromCartList.setOnClickListener(this);
        btnSetChanges.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnDeleteFromList:
                mInterface.delete(position);
                dismiss();
                break;

            case R.id.btnRestoreToCart:
                mInterface.addToCartList(item, position);
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
