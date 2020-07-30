package com.sparkle.devicescanner.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "b2b_duci_dev.db";
    private static final String JSON_TABLE_NAME = "protocolData";
    private static final String OVCAMP_TABLE_NAME = "ovCampData";
    private static final String LUMN_TABLE_NAME = "lumnData";
//    private static final String PROTOCOL_TABLE_NAME = "protocolType";

    private static final String COL_1 = "protocolType";
    private static final String COL_2 = "protocolJson";
    private static final String OVCAMP_COL_1 = "ovCampName";
    private static final String OVCAMP_COL_2 = "ovCampCommand";
    private static final String LUMN_COL_1 = "lumnName";
    private static final String LUMN_COL_2 = "lumnCommand";
//    private static final String PROTOCOL_COL_1 = "type";
//    private static final String PROTOCOL_COL_2 = "typeJson";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + JSON_TABLE_NAME + "(protocolType TEXT, protocolJson TEXT )");
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + OVCAMP_TABLE_NAME + "( ovCampName TEXT, ovCampCommand TEXT )");
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + LUMN_TABLE_NAME + "( lumnName TEXT, lumnCommand TEXT )");
//        db.execSQL(" CREATE TABLE IF NOT EXISTS " + PROTOCOL_TABLE_NAME + "( type TEXT, typeJson TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " +JSON_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " +OVCAMP_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " +LUMN_TABLE_NAME);
//        db.execSQL(" DROP TABLE IF EXISTS " +PROTOCOL_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertJsonData(String protocolType, String protocolJson){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1 , protocolType);
        contentValues.put(COL_2 , protocolJson);

        long result = db.insert(JSON_TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void updateJsonData(String protocolType, String protocolJson){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1 , protocolType);
        contentValues.put(COL_2 , protocolJson);
        db.update(JSON_TABLE_NAME,contentValues, " protocolType = ? ", new String[] {protocolType});
    }

    public boolean insertlumnData(String lumnName, String lumnCommand){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LUMN_COL_1 , lumnName);
        contentValues.put(LUMN_COL_2 , lumnCommand);

        long result = db.insert(LUMN_TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertovCampData(String ovCampName, String ovCampCommand){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OVCAMP_COL_1 , ovCampName);
        contentValues.put(OVCAMP_COL_2 , ovCampCommand);

        long result = db.insert(OVCAMP_TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

//    public boolean insertovPtotocolType(String type, String typeJson){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PROTOCOL_COL_1 , type);
//        contentValues.put(PROTOCOL_COL_2 , typeJson);
//
//        long result = db.insert(PROTOCOL_TABLE_NAME,null,contentValues);
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }

//    public Cursor getAllProtocolType(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery(" select * from " + PROTOCOL_TABLE_NAME ,  null);
//        return res;
//    }

    public Cursor getAlllumnData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + LUMN_TABLE_NAME ,  null);
        return res;
    }

    public Cursor getAllovCampData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + OVCAMP_TABLE_NAME ,  null);
        return res;
    }

//    public void updateProtocolJson(String type, String typeJson){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PROTOCOL_COL_2 , typeJson);
//        db.update(PROTOCOL_TABLE_NAME,contentValues, " type = ? ", new String[] {type});
//    }

//    public boolean insertPayAccountData(String payAccountID, String startDate,String depositDays, String payoffAmt, String minPayDays, String maxPayDays, String schPayDays, String userID, String distributorID,
//                              String assignedItemsID, String agentID, String agentAssignmentStatus, String agentAssignment, String initialCreditDays, String receivedPayAmt,
//                              String durationDays, String creditDaysIssued, String payDaysReceived){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_1 , payAccountID);
//        contentValues.put(COL_2 , startDate);
//        contentValues.put(COL_3 , depositDays);
//        contentValues.put(COL_4 , payoffAmt);
//        contentValues.put(COL_5 , minPayDays);
//        contentValues.put(COL_6 , maxPayDays);
//        contentValues.put(COL_7 , schPayDays);
//        contentValues.put(COL_8 , userID);
//        contentValues.put(COL_9 , distributorID);
//        contentValues.put(COL_10 , assignedItemsID);
//        contentValues.put(COL_11 , agentID);
//        contentValues.put(COL_12 , agentAssignmentStatus);
//        contentValues.put(COL_13 , agentAssignment);
//        contentValues.put(COL_14 , initialCreditDays);
//        contentValues.put(COL_15 , receivedPayAmt);
//        contentValues.put(COL_16 , durationDays);
//        contentValues.put(COL_17 , creditDaysIssued);
//        contentValues.put(COL_18 , payDaysReceived);
//        long result = db.insert(PAYACCOUNT_TABLE_NAME,null,contentValues);
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }
//
//    public boolean insertUserData(String userID, String distributorID,String userCode, String agentID, String lastName, String firstName, String phoneNumber, String email, String locationGPS,
//                                        String address1, String address2, String city, String state, String country, String postCode){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(USER_COL_1 , userID);
//        contentValues.put(USER_COL_2 , distributorID);
//        contentValues.put(USER_COL_3 , userCode);
//        contentValues.put(USER_COL_4 , agentID);
//        contentValues.put(USER_COL_5 , lastName);
//        contentValues.put(USER_COL_6 , firstName);
//        contentValues.put(USER_COL_7 , phoneNumber);
//        contentValues.put(USER_COL_8 , email);
//        contentValues.put(USER_COL_9 , locationGPS);
//        contentValues.put(USER_COL_10 ,address1);
//        contentValues.put(USER_COL_11 ,address2);
//        contentValues.put(USER_COL_12 ,city);
//        contentValues.put(USER_COL_13 ,state);
//        contentValues.put(USER_COL_14 ,country);
//        contentValues.put(USER_COL_15 ,postCode);
//
//        long result = db.insert(USER_TABLE_NAME,null,contentValues);
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }
//
//    public boolean insertPayEventData(String payEventID, String payEventDate,String payDays, String payRecordAmt, String payRecordNotes, String payAccountID,
//                                      String eventType, String codeDays, String codeIssued, String codehashTop,String sync){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PAYEVENT_COL_1 , payEventID);
//        contentValues.put(PAYEVENT_COL_2 , payEventDate);
//        contentValues.put(PAYEVENT_COL_3 , payDays);
//        contentValues.put(PAYEVENT_COL_4 , payRecordAmt);
//        contentValues.put(PAYEVENT_COL_5 , payRecordNotes);
//        contentValues.put(PAYEVENT_COL_6 , payAccountID);
//        contentValues.put(PAYEVENT_COL_7 , eventType);
//        contentValues.put(PAYEVENT_COL_8 , codeDays);
//        contentValues.put(PAYEVENT_COL_9 , codeIssued);
//        contentValues.put(PAYEVENT_COL_10 , codehashTop);
//        contentValues.put(PAYEVENT_COL_11 , sync);
//        long result = db.insert(PAYEVENT_TABLE_NAME,null,contentValues);
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }
//
//    public boolean insertProductItemData(String productItemID, String productModelID,String productBatchID, String productItemOEM_SN, String productItemPAYG_SN, String lifeCycleStatus,
//                                      String firmwareVersion, String assignedItemsID){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PRODUCT_COL_1 , productItemID);
//        contentValues.put(PRODUCT_COL_2 , productModelID);
//        contentValues.put(PRODUCT_COL_3 , productBatchID);
//        contentValues.put(PRODUCT_COL_4 , productItemOEM_SN);
//        contentValues.put(PRODUCT_COL_5 , productItemPAYG_SN);
//        contentValues.put(PRODUCT_COL_6 , lifeCycleStatus);
//        contentValues.put(PRODUCT_COL_7 , firmwareVersion);
//        contentValues.put(PRODUCT_COL_8 , assignedItemsID);
//
//        long result = db.insert(PRODUCTITEM_TABLE_NAME,null,contentValues);
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }
//
//    public boolean insertLastSyncData(String id,String payAccount,String payEvent,String user, String productItem){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(LAST_SYNC_COL_1 , id);
//        contentValues.put(LAST_SYNC_COL_2 , payAccount);
//        contentValues.put(LAST_SYNC_COL_3 , payEvent);
//        contentValues.put(LAST_SYNC_COL_4 , user);
//        contentValues.put(LAST_SYNC_COL_5 , productItem);
//
//        long result = db.insert(LAST_SYNC_TABLE_NAME,null,contentValues);
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }
//
//
//    public boolean insertBatchCodeData(String userID,String days){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(BATCH_CODE_COL_2 , userID);
//        contentValues.put(BATCH_CODE_COL_3 , days);
//
//        long result = db.insert(BATCH_CODE_TABLE_NAME,null,contentValues);
//        if(result == -1)
//            return false;
//        else
//            return true;
//    }

    public void deleteJsonData (String protocolType){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(JSON_TABLE_NAME, " protocolType = ? ", new String[]{protocolType});
    }




    public Cursor getJsonData(String s){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " select * from " + JSON_TABLE_NAME + " WHERE protocolType = ? ";
        Cursor cursor = db.rawQuery(query, new String[] {s});
        return cursor;
    }

    public Cursor getAllJsonData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + JSON_TABLE_NAME ,  null);
        return res;
    }

    public void deleteAlltable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DROP TABLE IF EXISTS " +JSON_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " +OVCAMP_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " +JSON_TABLE_NAME);
    }


 }
