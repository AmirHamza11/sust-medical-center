package com.example.sustmedicalcenter.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.controller.PrescriptionListAdapter;
import com.example.sustmedicalcenter.model.PrescriptionItem;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;

import java.util.ArrayList;

public class PrescriptionListDialog extends DialogFragment {
    private Button AddButton;
    ArrayList<PrescriptionItem> list;
    PrescriptionListAdapter adapter;
    private Context ctx;
    private AddMedicineDialog.onItemClickListener addMedicineItemClickListener;
    private String appointmentUid;
    private PrescriptionListAdapter.OnItemClicked prescriptionListAdapterListener;


    public static PrescriptionListDialog newInstance(Context ctx, AddMedicineDialog.onItemClickListener addMedicineItemClickListener, ArrayList<PrescriptionItem> list, String appointmentUid, PrescriptionListAdapter.OnItemClicked prescriptionListAdapterListener){
        PrescriptionListDialog prescriptionListDialog = new PrescriptionListDialog();
        prescriptionListDialog.ctx = ctx;
        prescriptionListDialog.addMedicineItemClickListener = addMedicineItemClickListener;
        prescriptionListDialog.list = list;
        prescriptionListDialog.appointmentUid = appointmentUid;
        prescriptionListDialog.prescriptionListAdapterListener = prescriptionListAdapterListener;
        return prescriptionListDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_prescription_list, container);
        RecyclerView recyclerView = view.findViewById(R.id.prescription_list_dialog_prescription_list_recyclerView_id);

        adapter = new PrescriptionListAdapter(list, ctx, prescriptionListAdapterListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setAdapter(adapter);

        AddButton = view.findViewById(R.id.prescription_list_dialog_add_new_medicine_button_id);

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicineDialog alertDialogue= AddMedicineDialog.newInstance(addMedicineItemClickListener,appointmentUid,adapter.getItemCount());
                alertDialogue.show(requireActivity().getSupportFragmentManager(), "");

            }
        });

        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("1")){
            AddButton.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public PrescriptionListAdapter getAdapter(){
        return adapter;
    }

    public String getAppointmentUid() {
        return appointmentUid;
    }



    @Override
    public void onResume() {
        super.onResume();

        int width = (int) (getResources().getDisplayMetrics().widthPixels *0.95);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}




