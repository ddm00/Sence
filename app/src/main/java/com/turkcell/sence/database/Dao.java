package com.turkcell.sence.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Dao {

    private static Dao uniqueInstance;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;


    public static Dao getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Dao();
        }
        return uniqueInstance;
    }

    public Dao() {
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}

