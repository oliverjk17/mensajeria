package com.optic.whatsappclone2.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.optic.whatsappclone2.models.Message;
import com.optic.whatsappclone2.models.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatusProvider {

    CollectionReference mCollection;

    public StatusProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Status");
    }

    public Task<Void> create(Status status) {
        DocumentReference document = mCollection.document();
        status.setId(document.getId());
        return document.set(status);
    }

    public Query getStatusByTimestampLimit() {
        long now = new Date().getTime();
        return mCollection.whereGreaterThan("timestampLimit", now);
    }

}
