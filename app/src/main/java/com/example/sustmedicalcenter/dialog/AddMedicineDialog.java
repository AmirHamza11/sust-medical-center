package com.example.sustmedicalcenter.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.PrescriptionItem;

public class AddMedicineDialog extends DialogFragment {

    private EditText Add_Medicine,Time_to_take;
    private AddMedicineDialog.onItemClickListener itemClickListener;
    private String appointmentUid;
    private int newPos;


    public interface onItemClickListener {
        void onAddingNewMedicine(PrescriptionItem item, String appointmentUid);
    }

    public static AddMedicineDialog newInstance(AddMedicineDialog.onItemClickListener itemClickListener, String appointmentUid,int newPos) {
        AddMedicineDialog dialogue = new AddMedicineDialog();
        dialogue.itemClickListener = itemClickListener;
        dialogue.appointmentUid = appointmentUid;
        dialogue.newPos = newPos;
        return dialogue;
    }


    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_medicine, null);
        Add_Medicine =view.findViewById(R.id.Add_Medicine);
        Time_to_take =view.findViewById(R.id.Time_to_take);


        builder.setView(view)
                .setTitle("Add New Medicine")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }

                });





        return builder.create();

    }

    @Override
    public void onResume() {
        super.onResume();

        AlertDialog ad = (AlertDialog) getDialog();

        if(ad!=null){
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Add_Medicine.getText().toString().equals("")){
                        Add_Medicine.setError("Enter Medicine Name");

                    }else Add_Medicine.setError(null);

                    if(Time_to_take.getText().toString().equals("")){
                        Time_to_take.setError("Enter Dosage:");
                        return;
                    }else Time_to_take.setError(null);

                    itemClickListener.onAddingNewMedicine(new PrescriptionItem((newPos+1) + " " + Add_Medicine.getText().toString(),"    " +Time_to_take.getText().toString()),appointmentUid);
                    dismiss();

                }
            });
        }


    }
}
