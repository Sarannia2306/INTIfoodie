package com.example.INTIFoodie;


import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// FirebaseUtils.java
public class FirebaseUtils {

    private static final String MENU_CATEGORY = "Category";

    public static void addOrUpdateMenuList(String categoryId, String itemName, double itemPrice, FirebaseCallback callback) {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference().child(MENU_CATEGORY);

        // Check if the category and item name are not empty
        if (!TextUtils.isEmpty(categoryId) && !TextUtils.isEmpty(itemName)) {
            DatabaseReference itemRef = menuRef.child(categoryId).child(itemName);

            // Set or update the item price
            itemRef.child("Price").setValue(itemPrice)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callback.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onFailure("Error: " + e.getMessage());
                        }
                    });
        } else {
            callback.onFailure("Invalid category or item name");
        }
    }

    // Callback interface for Firebase operations
    public interface FirebaseCallback {
        void onSuccess();
        void onFailure(String message);
    }
}



