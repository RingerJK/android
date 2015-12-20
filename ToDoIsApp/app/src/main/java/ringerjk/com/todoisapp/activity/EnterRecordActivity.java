package ringerjk.com.todoisapp.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ringerjk.com.todoisapp.R;
import ringerjk.com.todoisapp.adapter.CustomImageListViewAdapter;
import ringerjk.com.todoisapp.contentProvider.DBHelper;
import ringerjk.com.todoisapp.contentProvider.ToDoListProvider;
import ringerjk.com.todoisapp.models.Note;
import ringerjk.com.todoisapp.models.Picture;

public class EnterRecordActivity extends AppCompatActivity {
    final static String LOG_TAG = "myLogs";

    private int idNoteForUpdateOrDelete = 0;
    private File directory;

    EditText textTitle;
    EditText textDesc;
    ListView lvImage;
    private CustomImageListViewAdapter customImageAdapter;

    ArrayAdapter<Bitmap> bitmapArrayAdapter;
    private Cursor picCursor;

    final int TYPE_PHOTO = 1110;
    final int REQUEST_CODE_PHOTO = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entery_record);

        textTitle = (EditText) findViewById(R.id.textTitle);
        textDesc = (EditText) findViewById(R.id.textDesc);
        lvImage = (ListView) findViewById(R.id.lvImage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNoteForUpdateOrDelete = (int)extras.getLong(MainActivity.keyNodeId);
            String selection = "_id = ?";
            String[] selectionArgs = new String[]{String.valueOf(idNoteForUpdateOrDelete)};
            Cursor cursor = getContentResolver().query(
                    ToDoListProvider.NOTE_CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                textTitle.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE_NOTES)));
                textDesc.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_NOTES)));
                cursor.close();
            }
        } else {
            saveDataInDB(null);
        }

        picCursor = getContentResolver().query(ToDoListProvider.PICTURE_CONTENT_URI
                , null
                , DBHelper.KEY_NOTE_ID_PICTURES + " = ?"
                , new String[]{String.valueOf(idNoteForUpdateOrDelete)}
                , null);
        startManagingCursor(picCursor);

        customImageAdapter = new CustomImageListViewAdapter(this, new ArrayList<String>());
        lvImage.setAdapter(customImageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (picCursor.moveToFirst()) {
            ArrayList<String> bitmapArrayList = new ArrayList<>();
            String imgPath;
            do {
                imgPath = picCursor.getString(picCursor.getColumnIndex(DBHelper.KEY_IMAGE_PICTURES));
                bitmapArrayList.add(imgPath);
            } while (picCursor.moveToNext());
            customImageAdapter.updateListView(bitmapArrayList);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textTitle.getText().toString().trim().length() == 0){
            textTitle.setText("Note "+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
            saveDataInDB(null);
        }
    }

    public void composeEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, textTitle.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, textDesc.getText().toString());
        startActivity(emailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entery_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.ok:
                if (textTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Заполните поле Title", Toast.LENGTH_SHORT).show();
                } else {
                    saveDataInDB(null);
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.delete:
                Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, idNoteForUpdateOrDelete);
                int cnt = getContentResolver().delete(uri, null, null);
                Log.d(LOG_TAG, "delete, count = " + cnt);
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.mail:
                if (textTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Заполните поле Title", Toast.LENGTH_SHORT).show();
                } else {
                    composeEmail();
                    setResult(RESULT_OK, null);
                    finish();
                }
                break;
            case R.id.photo:
                createDirectory();
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_PHOTO);
                break;
            case R.id.notification:
                if (textTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Заполните поле Title", Toast.LENGTH_SHORT).show();
                } else {
                    saveDataInDB(null);
                    intent = new Intent(this, NotificationActivity.class);
                    intent.putExtra(MainActivity.keyNodeId, idNoteForUpdateOrDelete);
                    startActivity(intent);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult, requestCode =" + requestCode + "; data " + data + "; resultCode " + resultCode);
        switch (requestCode) {
            case REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    Uri uri = generateFileUri(TYPE_PHOTO);
                    Bitmap btm = (Bitmap) data.getExtras().get("data");
                    if (null != btm) {
                        try {
                            FileOutputStream out = new FileOutputStream(uri.getPath());
                            btm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                            saveDataInDB(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Btm: " + data.getData(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void saveDataInDB(Uri photoUri) {
        if (idNoteForUpdateOrDelete == 0) {
            Note note = new Note(textTitle.getText().toString(), textDesc.getText().toString());

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.KEY_TITLE_NOTES, note.getTitle());
            cv.put(DBHelper.KEY_DESCRIPTION_NOTES, note.getDescription());

            Uri uri = getContentResolver().insert(ToDoListProvider.NOTE_CONTENT_URI, cv);
            idNoteForUpdateOrDelete = Integer.parseInt((uri.getLastPathSegment()));
        } else {
            Note note = new Note(idNoteForUpdateOrDelete, textTitle.getText().toString(), textDesc.getText().toString());

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.KEY_TITLE_NOTES, note.getTitle());
            cv.put(DBHelper.KEY_DESCRIPTION_NOTES, note.getDescription());

            Uri uri = ContentUris.withAppendedId(ToDoListProvider.NOTE_CONTENT_URI, note.getId());
            int cnt = getContentResolver().update(uri, cv, null, null);
            idNoteForUpdateOrDelete = Integer.parseInt((uri.getLastPathSegment()));
        }
        if (photoUri != null) {
            Picture picture = new Picture(photoUri.getPath(), idNoteForUpdateOrDelete);
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.KEY_IMAGE_PICTURES, picture.getImageLink());
            cv.put(DBHelper.KEY_NOTE_ID_PICTURES, picture.getNote_id());
            Uri uri = getContentResolver().insert(ToDoListProvider.PICTURE_CONTENT_URI, cv);
        }
    }

    private Uri generateFileUri(int type) {
        File file = null;
        switch (type) {
            case TYPE_PHOTO:
                String namePh = "photo_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                file = new File(directory.getPath() + "/" + namePh);
                break;
        }
        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "ToDoListFolder");
        if (!directory.exists())
            directory.mkdirs();
    }
}
