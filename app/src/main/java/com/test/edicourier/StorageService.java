package com.test.edicourier;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.android.gms.iid.InstanceID;

public class StorageService {

    private final static String TAG = "SUPERLOG_StorageService";
    private final static String fileNameStorage = "setting";
    private final static String emptyString = "";

    public static void saveData(String data, Context context, String keyData) {
        byte[] encryptedValue = data.getBytes();
        String valueBase64String = Base64.encodeToString(encryptedValue, Base64.NO_WRAP);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileNameStorage, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = createKey(keyData, context);
        editor.putString(key, valueBase64String);
        editor.apply();
    }

    public static String getData(Context context, String keyData) {
        SharedPreferences preferences = context.getSharedPreferences(fileNameStorage, Context.MODE_PRIVATE);
        String key = createKey(keyData, context);
        String base64EncryptedString = preferences.getString(key, emptyString);

        byte[] encryptedBytes = Base64.decode(base64EncryptedString, Base64.NO_WRAP);
        String data = new String(encryptedBytes);

        if (emptyString.equals(data)) {
            return null;
        } else {
            return data;
        }
    }

    // Сохраненные данные шифруются уникальным идентификатором устройства + ключ-имя данных
    private static String createKey(String keyData, Context context) {
        String androidId = InstanceID.getInstance(context).getId();
        String key = androidId + "_" + keyData;
        byte[] encryptedKey = key.getBytes();
        return Base64.encodeToString(encryptedKey, Base64.NO_WRAP);
    }
}
