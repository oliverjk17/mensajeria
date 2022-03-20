package com.optic.whatsappclone2.providers;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.optic.whatsappclone2.models.Message;
import com.optic.whatsappclone2.models.Status;
import com.optic.whatsappclone2.utils.CompressorBitmapImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ImageProvider {

    StorageReference mStorage;
    FirebaseStorage mFirebaseStorage;
    int index;
    MessagesProvider mMessagesProvider;
    StatusProvider mStatusProvider;

    public ImageProvider() {
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorage = mFirebaseStorage.getReference();
        mMessagesProvider = new MessagesProvider();
        mStatusProvider = new StatusProvider();
        index = 0;
    }

    public UploadTask save(Context context, File file) {
        byte[] imageByte = CompressorBitmapImage.getImage(context, file.getPath(), 500, 500);
        StorageReference storage = mStorage.child(new Date() + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }

    public void uploadMultiple(final Context context, final ArrayList<Message> messages) {

        Uri[] uri = new Uri[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            File file = CompressorBitmapImage.reduceImageSize(new File(messages.get(i).getUrl()));

            uri[i] = Uri.parse("file://" + file.getPath());
            final StorageReference ref = mStorage.child(uri[i].getLastPathSegment());
            ref.putFile(uri[i]).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                messages.get(index).setUrl(url);
                                mMessagesProvider.create(messages.get(index));
                                index++;
                            }
                        });
                    }
                    else {
                        Toast.makeText(context, "Hubo un error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

    }

    public void uploadMultipleStatus(final Context context, final ArrayList<Status> statusList) {

        Uri[] uri = new Uri[statusList.size()];
        File file = CompressorBitmapImage.reduceImageSize(new File(statusList.get(index).getUrl()));

        uri[index] = Uri.parse("file://" + file.getPath());
        final StorageReference ref = mStorage.child(uri[index].getLastPathSegment());
        ref.putFile(uri[index]).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            statusList.get(index).setUrl(url);
                            mStatusProvider.create(statusList.get(index));
                            index++;

                            if (index < statusList.size()) {
                                uploadMultipleStatus(context, statusList);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(context, "Hubo un error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public Task<Uri> getDownloadUri() {
        return mStorage.getDownloadUrl();
    }

    public Task<Void> delete(String url) {
        return mFirebaseStorage.getReferenceFromUrl(url).delete();
    }


}
