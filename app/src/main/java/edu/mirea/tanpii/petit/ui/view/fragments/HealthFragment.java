package edu.mirea.tanpii.petit.ui.view.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.app.AlertDialog;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.UUID;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Document;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Treatment;
import edu.mirea.tanpii.petit.databinding.FragmentHealthBinding;
import edu.mirea.tanpii.petit.ui.view.adapters.DocumentsAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.MyPetsHealthAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.TreatmentsAdapter;
import edu.mirea.tanpii.petit.ui.viewmodel.HealthViewModel;

public class HealthFragment extends Fragment {

    final int PICK_DOCUMENT_REQUEST = 5;
    private String petUUID;
    private String documentURL;
    OnBackPressedCallback callback;
    HealthViewModel mViewModel;
    FragmentHealthBinding mBinding;

    TreatmentsAdapter.OnDeleteTreatmentClickListener onDeleteTreatmentClickListener = new TreatmentsAdapter.OnDeleteTreatmentClickListener() {
        @Override
        public void OnDeleteTreatmentClick(String uuid) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Вы действительно хотите удалить заметку?");

            builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.deleteTreatment(uuid);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    DocumentsAdapter.OnDeleteDocumentClickListener onDeleteDocumentClickListener = new DocumentsAdapter.OnDeleteDocumentClickListener() {
        @Override
        public void onDeleteDocumentClick(String uuid) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Вы действительно хотите удалить документ?");

            builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.deleteDocument(uuid);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    DocumentsAdapter.OnGoToDocumentClickListener onGoToDocumentClickListener = new DocumentsAdapter.OnGoToDocumentClickListener() {
        @Override
        public void onGoToDocumentClick(String documentURL, View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(documentURL), "application/pdf");
            startActivity(intent);
        }
    };

    MyPetsHealthAdapter.OnPetClickListener onPetClickListener = new MyPetsHealthAdapter.OnPetClickListener() {
        @Override
        public void onPetClick(Pet pet) {
                mBinding.titleHealth.setText("ЗДОРОВЬЕ " + pet.getName().toUpperCase());
                petUUID = pet.getUUID();
                mViewModel.getDocuments(pet).observe(getViewLifecycleOwner(), new Observer<List<Document>>() {
                    @Override
                    public void onChanged(List<Document> documents) {
                        ((DocumentsAdapter) mBinding.recyclerViewDocuments.getAdapter()).updateData(documents);
                        if (documents.size() == 0) {
                            mBinding.imgNoDocuments.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.documents, null));
                            mBinding.txtNoDocuments.setText(R.string.hint_add_document);
                        }
                        else{
                            mBinding.imgNoDocuments.setImageDrawable(null);
                            mBinding.txtNoDocuments.setText("");
                        }
                    }
                });
                mViewModel.getTreatments(pet).observe(getViewLifecycleOwner(), new Observer<List<Treatment>>() {
                    @Override
                    public void onChanged(List<Treatment> treatments) {
                        ((TreatmentsAdapter) mBinding.recyclerViewTreatments.getAdapter()).updateData(treatments);
                        if (treatments.size() == 0) {
                            mBinding.imgNoTreatments.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.treatments, null));
                            mBinding.txtNoTreatments.setText(R.string.hint_add_treatment);
                        }
                        else{
                            mBinding.imgNoTreatments.setImageDrawable(null);
                            mBinding.txtNoTreatments.setText("");
                        }
                    }
                });
                mBinding.newTreatment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("petUUID", pet.getUUID());
                        Navigation.findNavController(v).navigate(R.id.newTreatmentDialog, bundle);
                    }
                });
                mBinding.newDocument.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.putExtra("petUUID", pet.getUUID());
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(
                                    intent,
                                    "Select document from here..."),
                            PICK_DOCUMENT_REQUEST);
            });
        }
    };

    public static HealthFragment newInstance() {
        return new HealthFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentHealthBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_global_healthFragment);
            }
        };

        mViewModel = new ViewModelProvider(this).get(HealthViewModel.class);

        mBinding.recyclerViewPets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mBinding.recyclerViewPets.setAdapter(new MyPetsHealthAdapter(onPetClickListener));

        mBinding.recyclerViewDocuments.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewTreatments.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
            ((MyPetsHealthAdapter) mBinding.recyclerViewPets.getAdapter()).updateData(pets);
            if (pets.size() != 0) {
                mBinding.titleHealth.setText("ЗДОРОВЬЕ " + pets.get(0).getName().toUpperCase());
                mBinding.recyclerViewTreatments.setAdapter(new TreatmentsAdapter(onDeleteTreatmentClickListener));
                mBinding.recyclerViewDocuments.setAdapter(new DocumentsAdapter(onGoToDocumentClickListener, onDeleteDocumentClickListener));
                mViewModel.getDocuments(pets.get(0)).observe(getViewLifecycleOwner(), new Observer<List<Document>>() {
                    @Override
                    public void onChanged(List<Document> documents) {
                        ((DocumentsAdapter) mBinding.recyclerViewDocuments.getAdapter()).updateData(documents);
                        if (documents.size() == 0) {
                            mBinding.imgNoDocuments.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.documents, null));
                            mBinding.txtNoDocuments.setText(R.string.hint_add_document);
                        }
                        else{
                            mBinding.imgNoDocuments.setImageDrawable(null);
                            mBinding.txtNoDocuments.setText("");
                        }

                    }
                });
                mViewModel.getTreatments(pets.get(0)).observe(getViewLifecycleOwner(), new Observer<List<Treatment>>() {
                    @Override
                    public void onChanged(List<Treatment> treatments) {
                        ((TreatmentsAdapter) mBinding.recyclerViewTreatments.getAdapter()).updateData(treatments);
                        if (treatments.size() == 0) {
                            mBinding.imgNoTreatments.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.treatments, null));
                            mBinding.txtNoTreatments.setText(R.string.hint_add_treatment);
                        }
                        else{
                            mBinding.imgNoTreatments.setImageDrawable(null);
                            mBinding.txtNoTreatments.setText("");
                        }
                    }
                });
                mBinding.newTreatment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("petUUID", pets.get(0).getUUID());
                        Navigation.findNavController(v).navigate(R.id.newTreatmentDialog, bundle);
                    }
                });
                mBinding.newDocument.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.putExtra("petUUID", pets.get(0).getUUID());
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(
                                    intent,
                                    "Select document from here..."),
                            PICK_DOCUMENT_REQUEST);
                });
            }
            else {
                mBinding.titleDocuments.setVisibility(View.GONE);
                mBinding.titleTreatment.setVisibility(View.GONE);
                mBinding.newDocument.setVisibility(View.GONE);
                mBinding.newTreatment.setVisibility(View.GONE);
                mBinding.titleHealth.setText("ЗДОРОВЬЕ ПИТОМЦЕВ");
            }
            mBinding.btnDiary.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_global_myDiaryFragment);
            });
            mBinding.btnPets.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
            });
            mBinding.btnProfile.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_global_myProfileFragment);
            });
        });
    }

    ProgressDialog dialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.setEnabled(true);
        if (resultCode == RESULT_OK) {

            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Загрузка файла...");

            dialog.show();
            Uri uri = data.getData();
            final String fileName = "" + Data.getTgetFileName(uri);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            final StorageReference filepath = storageReference.child(fileName + "." + "pdf");
            Toast.makeText(getContext(), filepath.getName(), Toast.LENGTH_SHORT).show();
            filepath.putFile(uri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Uri resultUri = task.getResult();
                        documentURL = resultUri.toString();
                        String uuid = UUID.randomUUID().toString();
                        String title = getFileName(uri);
                        mViewModel.addDocument(uuid, petUUID, title, documentURL);
                        Toast.makeText(getContext(), "Удачно загружено", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            callback.setEnabled(false);
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}

