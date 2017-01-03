package arsy.com.topperandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventListDb";

    private static final String TABLE_EVENTS = "eventLists";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_FAVOURITE = "favourite";
    Context ctx;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        ctx = context;
    }

    // Creating Tables  
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TRABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_CATEGORY + " TEXT DEFAULT NULL,"
                + KEY_DESCRIPTION + " TEXT DEFAULT NULL,"
                + KEY_EXPERIENCE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_FAVOURITE + " INTEGER DEFAULT 0"
                + ")";

        db.execSQL(CREATE_CONTACTS_TRABLE);
    }

    // Upgrading database  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
                // Create tables again
                onCreate(db);
                break;
            case 2:
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
                // Create tables again
                onCreate(db);
                break;
            case 3:
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
                // Create tables again
                onCreate(db);
                break;
            default:

        }

    }


    public Cursor getEventList() {
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public void deleteAllEventsInfo() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_EVENTS, 1 + "=" + 1, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertEventInfo(String id,String name
            , String image
            , String category
            , String description
            , String experience) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
        cv.put(KEY_NAME, name);
        cv.put(KEY_IMAGE, image);
        cv.put(KEY_CATEGORY, category);
        cv.put(KEY_DESCRIPTION, description);
        cv.put(KEY_EXPERIENCE, experience);
        db.insert(TABLE_EVENTS, null, cv);
    }

    public void updateFavourite(int favourite, String id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_FAVOURITE, favourite);
            db.update(TABLE_EVENTS, cv, KEY_ID + "='" + id + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor getEvent(String Id) {
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS+ " where " + KEY_ID + "='" + Id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getFavouriteEventList() {
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS+ " where " + KEY_FAVOURITE + "='" + 1 + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int getRowsCountForCategory(String category){
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS+ " where " + KEY_CATEGORY + "='" + category+ "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }
    public Cursor selectDistinctValues(){
        String selectQuery = "SELECT DISTINCT "+ KEY_CATEGORY+" FROM "+TABLE_EVENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
}