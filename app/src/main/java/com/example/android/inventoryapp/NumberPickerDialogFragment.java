package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by Chris on 12/21/2016.
 */

public class NumberPickerDialogFragment extends DialogFragment {
    private static final String DIALOG_TITLE = "dialogName";
    private static final String DIALOG_BUTTON_TEXT_INT = "buttonText";
    private static final String DIALOG_TYPE_ID = "dialogId";

    // Listener for closing dialog on positive button click
    private OnCompleteListener mListener;

    public static NumberPickerDialogFragment newInstance(
            int dialogId, String title, int buttonString) {

        Bundle bundle = new Bundle();
        bundle.putInt(DIALOG_TYPE_ID, dialogId);
        bundle.putString(DIALOG_TITLE, title);
        bundle.putInt(DIALOG_BUTTON_TEXT_INT, buttonString);

        NumberPickerDialogFragment fragment = new NumberPickerDialogFragment();

        fragment.setArguments(bundle);

        return fragment;
    }

    // http://stackoverflow.com/questions/15121373/returning-string-from-dialog-fragment-back-to-activity
    // Complete Listener for dialogs answer from Kirk
    public static interface OnCompleteListener {
        public abstract void onComplete(int dialogId, int quantity);
    }

    // make sure the Activity implemented it
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnCompleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        final int dialogId = bundle.getInt(DIALOG_TYPE_ID);
        String title = bundle.getString(DIALOG_TITLE);

        int buttonText = bundle.getInt(DIALOG_BUTTON_TEXT_INT);

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.number_picker_dialog, null);
        // Setup number picker
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(500);
        numberPicker.setWrapSelectorWheel(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle(title);

        builder.setPositiveButton(getString(buttonText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear focus tip from Dvd Franco
                // http://stackoverflow.com/questions/3691099/android-numberpicker-not-saving-edittext-changes
                numberPicker.clearFocus();
                int quantity = numberPicker.getValue();

                // Send result back to activity
                mListener.onComplete(dialogId, quantity);
            }
        });

        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }
}
