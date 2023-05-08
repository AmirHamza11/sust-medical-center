package com.example.sustmedicalcenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements View.OnClickListener{

    CircleImageView userDisplayPicture;
    TextView userNameTextView, registrationTextView;
    CardView appointmentCardView, newsCardView, bloodGroupCardView, servicesCardView, infoCardView, settingsCardView;

    private String currentUserType;

    // Required empty public constructor
    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        userDisplayPicture = v.findViewById(R.id.home_fragment_display_picture_id);
        userNameTextView = v.findViewById(R.id.home_fragment_username_id);
        registrationTextView = v.findViewById(R.id.home_fragment_registration_id);

        appointmentCardView = v.findViewById(R.id.home_fragment_appointment_cardView_id);
        newsCardView = v.findViewById(R.id.home_fragment_news_cardView_id);
        bloodGroupCardView = v.findViewById(R.id.home_fragment_blood_group_cardView_id);
        servicesCardView = v.findViewById(R.id.home_fragment_services_cardView_id);
        infoCardView = v.findViewById(R.id.home_fragment_info_cardView_id);
        settingsCardView = v.findViewById(R.id.home_fragment_settings_cardView_id);

        appointmentCardView.setOnClickListener(this);
        newsCardView.setOnClickListener(this);
        bloodGroupCardView.setOnClickListener(this);
        servicesCardView.setOnClickListener(this);
        infoCardView.setOnClickListener(this);
        settingsCardView.setOnClickListener(this);


        currentUserType = CurrentUserSingleton.getInstance().getCurrentUser().getUserType();



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        userNameTextView.setText(CurrentUserSingleton.getInstance().getCurrentUser().getUserName());
        if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("0")){
            registrationTextView.setText(CurrentUserSingleton.getInstance().getCurrentUser().getRegistrationNo());
        }else {
            registrationTextView.setText(CurrentUserSingleton.getInstance().getCurrentUser().getDoctorDetails());
        }

        if(!CurrentUserSingleton.getInstance().getCurrentUser().getDisplayImageUrl().equals("")){
            Picasso.get()
                    .load(CurrentUserSingleton.getInstance().getCurrentUser().getDisplayImageUrl())
                    .placeholder(R.drawable.d1)
                    .into(userDisplayPicture);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_fragment_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.home_fragment_requested_users_menu_option_id);
        if(!CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("2")){
            menuItem.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        if (item.getItemId() == R.id.home_fragment_notifications_menu_option_id) {
            Toast.makeText(getContext(), "notifications", Toast.LENGTH_SHORT).show();
            return true;
        }else if(item.getItemId() == R.id.home_fragment_requested_users_menu_option_id){
            Intent accountRequestsIntent = new Intent(getActivity(),AccountRequestsActivity.class);
            startActivity(accountRequestsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_fragment_appointment_cardView_id:
                Intent intent;
                if(currentUserType.equals("0")) {
                    intent = new Intent(getActivity(), StudentAppointmentListActivity.class);
                    startActivity(intent);

                }else if(currentUserType.equals("1")) {

                    intent = new Intent(getActivity(), StudentAppointmentListActivity.class);
                    startActivity(intent);

                }else if(currentUserType.equals("2")){
                    intent = new Intent(getActivity(), ModeratorAppointmentsListActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.home_fragment_news_cardView_id:
            {
                intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.home_fragment_blood_group_cardView_id:
            {
                intent = new Intent(getActivity(), BloodActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.home_fragment_services_cardView_id:
            {
                intent = new Intent(getActivity(), Services.class);
                startActivity(intent);
            }
                break;
            case R.id.home_fragment_info_cardView_id:
            {
                Uri uri = Uri.parse("https://www.sust.edu/offices/other-offices/14");
                Intent intentinfo = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentinfo);
            }
                break;
            case R.id.home_fragment_settings_cardView_id:
            {
                intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }

                break;
            default:
                break;
        }

    }

    public void showToast(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();

    }
}