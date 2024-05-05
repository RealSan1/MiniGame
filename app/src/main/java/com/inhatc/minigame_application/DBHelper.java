package com.inhatc.minigame_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//SQLite를 이용한 데이터베이스
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    //안드로이드 내부에 저장되는 데이터베이스 파일
    private static final String DATABASE_NAME = "gameTable.db";
    //데이터베이스 파일의 수정버전 여러번 수정할 수록 버전 수 증가
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "q_a_table";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static synchronized DBHelper getInstance(Context context){//싱글톤 패턴
        if(instance == null){
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }
    // 문제와 답을 추가하는 메서드
    public void insertQuestionAndAnswer(String question, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("qa_question", question);
        values.put("qa_answer", answer);
        db.insert("q_a_table", null, values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//데이터베이스내 테이블 생성 함수
        // q_a_table 테이블 생성 쿼리
        String CREATE_TABLE_QA = "CREATE TABLE q_a_table (" +
                "qa_code INTEGER PRIMARY KEY AUTOINCREMENT," +
                "qa_question TEXT," +
                "qa_answer TEXT" +
                ")";
        db.execSQL(CREATE_TABLE_QA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {//특정 테이블 업그레이드 함수
        String sql = "DROP TABLE if exists mytable";

        db.execSQL(sql);
        onCreate(db);
    }
    public Cursor getAllData(){//전체 검색
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME, // 테이블 이름
                null,       // 검색할 열의 배열 (null이면 모든 열을 반환)
                null,       // 조건 절 (null이면 조건 없음)
                null,       // 조건 절의 값 (null이면 조건 없음)
                null,       // GROUP BY 절 (null이면 그룹화하지 않음)
                null,       // HAVING 절 (null이면 HAVING 절 없음)
                null        // ORDER BY 절 (null이면 정렬하지 않음)
        );
        return cursor;
    }

    public Cursor getFilteredData(String filter){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_NAME; /* + " WHERE " + COLUMN_NAME + " = ?";*/
        String[] selectionArgs = { filter };
        Cursor cursor = db.rawQuery(sqlQuery, selectionArgs);
        return cursor;
    }

}
