package com.optic.whatsappclone2.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.optic.whatsappclone2.models.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagesProvider {

    CollectionReference mCollection;

    public MessagesProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Messages");
    }

    public Task<Void> create(Message message) {
        DocumentReference document = mCollection.document();
        message.setId(document.getId());
        return document.set(message);
    }

    public Query getMessagesByChat(String idChat) {
        return mCollection.whereEqualTo("idChat", idChat).orderBy("timestamp", Query.Direction.ASCENDING);
    }

    public DocumentReference getMessagesById(String idMessage) {
        return mCollection.document(idMessage);
    }

    public Query getLastMessagesByChatAndSender(String idChat, String idSender) {
        ArrayList<String> status = new ArrayList<>();
        status.add("ENVIADO");
        status.add("RECIBIDO");

        return mCollection
                .whereEqualTo("idChat", idChat)
                .whereEqualTo("idSender", idSender)
                .whereIn("status", status)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5);
    }

    public Task<Void> updateStatus(String idMessage, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        return mCollection.document(idMessage).update(map);
    }

    public Query getMessagesNotRead(String idChat) {
        ArrayList<String> status = new ArrayList<>();
        status.add("ENVIADO");
        status.add("RECIBIDO");
        return mCollection
                .whereEqualTo("idChat", idChat)
                .whereIn("status", status);
    }

    public Query getReceiverMessagesNotRead(String idChat, String idReceiver) {
        ArrayList<String> status = new ArrayList<>();
        status.add("ENVIADO");
        status.add("RECIBIDO");
        return mCollection
                .whereEqualTo("idChat", idChat)
                .whereIn("status", status)
                .whereEqualTo("idReceiver", idReceiver);
    }

    public Query getLastMessage(String idChat) {
        return mCollection.whereEqualTo("idChat", idChat).orderBy("timestamp", Query.Direction.DESCENDING).limit(1);
    }

}
