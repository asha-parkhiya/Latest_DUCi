package com.sparkle.devicescanner.ContentProvider;

import android.net.Uri;

/**
 * Created by anildeshpande on 4/30/17.
 */

public interface ToDoProviderConstants {

    Uri CONTENT_URI_1 = Uri.parse
            ("content://com.sparkle.roam.ContentProvider/PPID_LIST");
    String COLUMN_ID = "ppid";
}
