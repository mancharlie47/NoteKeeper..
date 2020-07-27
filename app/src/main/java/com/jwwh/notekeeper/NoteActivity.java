package com.jwwh.notekeeper;

import android.content.Intent;
//import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private static final String TAG = "NoteKeeper";
    private static final String NOTE_POSITION = "com.jwwh.notekeeper.NOTE_POSITION";
    private static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteText;
    private EditText mTextNoteTitle;

    private boolean mIsCancelling;
    // private NoteKeeperOpenHelper mDbOpenHelper;
 //   private Cursor mNoteCursor;
   // private int mCourseIdPos;
   // private int mNoteTitlePos;
 //   private int mNoteTextPos;
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.jwwh.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.jwwh.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.jwwh.notekeeper.ORIGINAL_NOTE_TEXT";
    public String mOriginalNoteCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;
    private int mNotePosition;


    //  @Override
 //   protected void onDestroy() {
     //   mDbOpenHelper.close();
    //    super.onDestroy();
   // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //    mDbOpenHelper = new NoteKeeperOpenHelper(this);



        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValue();
        if(savedInstanceState == null) {
            saveOriginalNoteValue();
        }else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        mTextNoteText = findViewById(R.id.text_note_text);
        mTextNoteTitle = findViewById(R.id.text_note_title);
        if(!mIsNewNote)
          displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
      //  LoadNoteData();
        Log.d(TAG, "onCreate");

    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
       mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
       mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
       mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);

    }

   // private void LoadNoteData() {
   //     SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
//
  //      String courseId = "android_intents";
    //    String titleStart = "dynamic";
//
  //      String selection = NoteInfoEntry._ID + " = ?";
//
  //      String[] selectionArgs = {Integer.toString(mNoteId)};
//
  //      String[] noteColumn = {
    //            NoteInfoEntry.COLUMN_COURSE_ID,
      //          NoteInfoEntry.COLUMN_NOTE_TITLE,
        //        NoteInfoEntry.COLUMN_NOTE_TEXT,

        //};
       // mNoteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumn,
         //       selection, selectionArgs,null,null,null);
        //mCourseIdPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        //mNoteTitlePos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
       // mNoteTextPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
       // mNoteCursor.moveToNext();
        //displayNote();
    //}

    private void saveOriginalNoteValue() {

        if(mIsNewNote)
            return;
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getText();


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mIsCancelling) {
            if (mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                storePreviousNoteValues();
            }
        }   else{
                saveNote();
            }
        }


    private void storePreviousNoteValues() {

        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);

        mNote.setCourse(course);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle((mTextNoteTitle.getText().toString()));
        mNote.setText(mTextNoteText.getText().toString());
    }
    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
       // String courseId = mNoteCursor.getString(mCourseIdPos);
        //String noteTitle = mNoteCursor.getString(mNoteTitlePos);
        // String noteText = mNoteCursor.getString(mNoteTextPos);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        //CourseInfo course = DataManager.getInstance().getCourse(courseId);
        int courseIndex = courses.indexOf(mNote.getCourse());
        mSpinnerCourses.setSelection(courseIndex);
        mTextNoteTitle.setText(mNote.getTitle());
        mTextNoteText.setText(mNote.getText());
    }


    private void readDisplayStateValue() {
        Intent intent = getIntent();
        mNotePosition= intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = mNotePosition == POSITION_NOT_SET;
        if(mIsNewNote){
            createNewNote();

        }
        Log.i(TAG, "mNotePosition: " + mNotePosition);
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }else if(id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }else if(id == R.id.action_next){
            moveNext();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() -1;
        item.setEnabled(mNotePosition < lastNoteIndex);

        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();

        ++mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
        saveOriginalNoteValue();
        displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
        invalidateOptionsMenu();

    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout what I learned in the Pluralsight course\"" +
                course.getTitle()+"\"\n" + mTextNoteText.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);
    }
}
