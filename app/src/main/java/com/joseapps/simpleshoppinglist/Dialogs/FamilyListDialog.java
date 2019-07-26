package com.joseapps.simpleshoppinglist.Dialogs;

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

import com.joseapps.simpleshoppinglist.ActivityFamilyList.FamilyListActivity;
import com.joseapps.simpleshoppinglist.Interface.FamilyListInterface;
import com.joseapps.simpleshoppinglist.Models.Item;
import com.joseapps.simpleshoppinglist.R;
import com.joseapps.simpleshoppinglist.utils.NotNull;

public class FamilyListDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "FamilyListDialog";
    private FamilyListInterface mInterface;

    private EditText etItemName, etListName, etPrice, etQuanity;
    private Item item;
    private int position;

    public static FamilyListDialog newInstance(Item item, int position) {
        FamilyListDialog frag = new FamilyListDialog();
        frag.setItems(item, position);
        return frag;
    }

    private void setItems(Item item, int position) {
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

        if (item != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            referenceWidgets(view);
            mInterface = (FamilyListActivity) getContext();

            item = NotNull.item(item);
            Log.d(TAG, "onCreateView: non Null item = " + item.toString());

            etListName.setText(item.getList_name());
            etItemName.setText(item.getItem_name());
            String stPrice = item.getPrice().toString();
            etPrice.setText(stPrice);
            String stQuantity = "" + item.getQuantity();
            etQuanity.setText(stQuantity);

            etListName.setKeyListener(null);
            etItemName.setKeyListener(null);
            etPrice.setKeyListener(null);
            etQuanity.setKeyListener(null);

        }
        return view;
    }

    private void referenceWidgets(View view){

        Button btnDeleteFromCartList = view.findViewById(R.id.btnDeleteFromList);
        Button btnAcceptIntoCart = view.findViewById(R.id.btnAcceptIntoCart);

        etPrice = view.findViewById(R.id.etItemPrice);
        etItemName = view.findViewById(R.id.etItemName);
        etListName = view.findViewById(R.id.etListName);
        etQuanity = view.findViewById(R.id.etQuantity);

        btnAcceptIntoCart.setOnClickListener(this);
        btnDeleteFromCartList.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnDeleteFromList:
                mInterface.delete(position);
                dismiss();
                break;

            case R.id.btnAcceptIntoCart:
                mInterface.addToCartList(item, position);
                dismiss();
                break;

        }
    }
}
