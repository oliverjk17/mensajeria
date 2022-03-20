package com.optic.whatsappclone2.providers;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.optic.whatsappclone2.models.StatusViewer;

public class StatusViewerProvider {

    CollectionReference mCollection;
    AuthProvider mAuthProvider;

    public StatusViewerProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("StatusViewer");
        mAuthProvider = new AuthProvider();
    }

    public void create(final StatusViewer statusViewer) {
        mCollection.document(statusViewer.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    mCollection.document(statusViewer.getId()).set(statusViewer);
                }
            }
        });
    }

    public DocumentReference getStoryViewerById(String id) {
        return mCollection.document(id);
    }

}
