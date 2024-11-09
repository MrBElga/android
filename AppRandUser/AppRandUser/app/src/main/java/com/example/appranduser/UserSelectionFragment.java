package com.example.appranduser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSelectionFragment extends Fragment {

    private ImageView userImageView;
    private TextView userNameTextView;
    private TextView userAgeTextView;
    private Spinner genderSpinner;
    private Spinner nationalitySpinner;
    private View rootView;
    private Result userResult;
    private Button locationButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_selection, container, false);
        locationButton = rootView.findViewById(R.id.view_location_button);
        userImageView = rootView.findViewById(R.id.user_image);
        userNameTextView = rootView.findViewById(R.id.user_name);
        userAgeTextView = rootView.findViewById(R.id.user_age);
        genderSpinner = rootView.findViewById(R.id.gender_spinner);
        nationalitySpinner = rootView.findViewById(R.id.nationality_spinner);

        // Configuração dos Spinners
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genders, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> nationalityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.nationalities, android.R.layout.simple_spinner_item);
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalitySpinner.setAdapter(nationalityAdapter);

        Button loadButton = rootView.findViewById(R.id.get_user);
        loadButton.setOnClickListener(v -> loadUserData());
        locationButton.setOnClickListener(v -> {
            if (userResult == null || userResult.name == null || userResult.dob == null || userResult.location == null) {
                // Se o usuário não for válido, exibe o Toast
                Toast.makeText(getActivity(), "DEVE ESCOLHER UM USUARIO", Toast.LENGTH_LONG).show();
            } else {
                // Se o usuário for válido, realiza a navegação para a localização
                Bundle bundle = new Bundle();
                bundle.putString("latitude", userResult.location.coordinates.latitude);
                bundle.putString("longitude", userResult.location.coordinates.longitude);
                Navigation.findNavController(rootView).navigate(R.id.action_userSelection_to_userLocation, bundle);
            }
        });

        return rootView;
    }

    private void loadUserData() {
        String selectedGender = genderSpinner.getSelectedItem().toString().toLowerCase();
        String selectedNationality = nationalitySpinner.getSelectedItem().toString().toLowerCase();

        Call<User> call = new RetrofitConfig().getUserService().getUser(selectedGender, selectedNationality);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Result userResult = user.results.get(0);

                    // Verifica se os dados essenciais estão presentes
                    if (userResult != null && userResult.name != null && userResult.dob != null && userResult.location != null) {
                        // Exibir o nome completo
                        userNameTextView.setText(userResult.name.first + " " + userResult.name.last);

                        // Exibir a idade
                        userAgeTextView.setText("Idade: " + userResult.dob.age);

                        // Carregar a imagem do usuário
                        Picasso.get().load(userResult.picture.large).into(userImageView);

                        // Habilitar navegação para localização
                        rootView.findViewById(R.id.view_location_button).setOnClickListener(v -> {
                            if (userResult.location.coordinates != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("latitude", userResult.location.coordinates.latitude);
                                bundle.putString("longitude", userResult.location.coordinates.longitude);
                                Navigation.findNavController(rootView).navigate(R.id.action_userSelection_to_userLocation, bundle);
                            }
                        });
                    } else {
                        // Exibe o Toast se algum dado essencial estiver faltando
                        Toast.makeText(getActivity(), "DEVE ESCOLHER UM USUARIO", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Exibe o Toast se a resposta não for bem-sucedida
                    Toast.makeText(getActivity(), "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API Error", "Failed to load user data: " + t.getMessage());
                // Exibe o Toast em caso de falha na comunicação com a API
                Toast.makeText(getActivity(), "Falha ao se comunicar com a API", Toast.LENGTH_LONG).show();
            }
        });
    }



}
