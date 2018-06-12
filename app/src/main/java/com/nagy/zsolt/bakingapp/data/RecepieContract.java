package com.nagy.zsolt.bakingapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecepieContract {
    public static final String CONTENT_AUTHORITY = "com.nagy.zsolt.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECEPIES = "recepies";
    public static final String PATH_INGREDIENTS = "ingredients";

    public static final class RecepieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECEPIES)
                .build();

        public static Uri buildRecepieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "Recepie";
        public static final String COLUMN_RECEPIE_ID = "id";
        public static final String COLUMN_RECEPIE_NAME = "name";
    }

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENTS)
                .build();

        public static Uri buildRecepieUri(long id) {
            return android.content.ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static final String TABLE_NAME = "Ingredients";
        public static final String COLUMN_RECEPIE_NAME = "name";
        public static final String COLUMN_RECEPIE_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";
    }
}
