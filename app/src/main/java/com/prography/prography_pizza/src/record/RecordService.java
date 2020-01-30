package com.prography.prography_pizza.src.record;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prography.prography_pizza.src.record.interfaces.RecordActivityView;
import com.prography.prography_pizza.src.record.interfaces.RecordRetrofitInterface;
import com.prography.prography_pizza.src.record.models.RecordRequest;
import com.prography.prography_pizza.src.record.models.RecordResponse;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.prography_pizza.src.ApplicationClass.BASE_FIREBASE_STORAGE;
import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class RecordService {
    final RecordActivityView mRecordActivityView;

    public RecordService(RecordActivityView mRecordActivityView) {
        this.mRecordActivityView = mRecordActivityView;
    }

    public void postRecord(int challengeId, double totalTime, double totalDistance, String latLngs) {
        final RecordRetrofitInterface recordRetrofitInterface = getRetrofit().create(RecordRetrofitInterface.class);
        recordRetrofitInterface.postRecord(new RecordRequest(challengeId, totalTime, totalDistance, latLngs)).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(Call<RecordResponse> call, Response<RecordResponse> response) {
                final RecordResponse recordResponse = response.body();
                if (recordResponse == null) {
                    mRecordActivityView.validateFailure(null);
                    return;
                    // Fail
                }
                // Success
                mRecordActivityView.validateSuccess(String.valueOf(recordResponse.getData().get(0).getRecordId()));
            }

            @Override
            public void onFailure(Call<RecordResponse> call, Throwable t) {
                t.printStackTrace();
                mRecordActivityView.validateSuccess(null);
                // Fail
            }
        });
    }

    public void postImgToFirebase(String userName, Bitmap bitmap) {
        Date date = new Date();
        // FirebaseStorage 인스턴스
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance(BASE_FIREBASE_STORAGE);
        StorageReference mImageRef = mFirebaseStorage.getReference().child("imgs"); // imgs 폴더 참조.

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        final String title = Base64.encodeToString(userName.getBytes(), Base64.NO_WRAP) + "_" + date.getTime() + ".jpg"; // 특수문자 고려하여 이름을 Base64로 인코딩
        mImageRef.child(title).putBytes(data)
                .addOnFailureListener((e) -> {
                    mRecordActivityView.validateFailure("firebase Failure");
                    e.printStackTrace();
                })
                .addOnSuccessListener(taskSnapshot -> mRecordActivityView.validateFirebaseSuccess(title));
    }
}
