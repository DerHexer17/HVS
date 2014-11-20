package com.example.datahandling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper sInstance;
	private SQLiteDatabase db = null;

	// Logcat tag
	private static final String TAG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 11;

	// Database Name
	private static final String DATABASE_NAME = "hvsData";

	// Table Names
	private static final String TABLE_SPIELE = "spiel";
	private static final String TABLE_LOG = "log";
	private static final String TABLE_LIGA = "liga";
	private static final String TABLE_SPIELTAG = "spieltag";
	private static final String TABLE_HALLE = "halle";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	// SPIELE Table - column names
	private static final String SPIEL_NR = "spiel_nr";
	private static final String SPIEL_DATE = "spiel_date";
	private static final String SPIEL_TIME = "spiel_time";
	private static final String SPIEL_TEAM_HEIM = "spiel_team_heim";
	private static final String SPIEL_TEAM_GAST = "spiel_team_gast";
	private static final String SPIEL_TORE_HEIM = "spiel_tore_heim";
	private static final String SPIEL_TORE_GAST = "spiel_tore_gast";
	private static final String SPIEL_PUNKTE_HEIM = "spiel_punkte_heim";
	private static final String SPIEL_PUNKTE_GAST = "spiel_punkte_gast";
	private static final String SPIEL_SR = "schiedsrichter";
	private static final String SPIEL_HALLE = "spiel_halle";
	private static final String SPIEL_LIGA_NR = "spiel_liga_nr";
	private static final String SPIEL_SPIELTAG_NR = "spiel_spieltag_nr";
	private static final String SPIEL_SPIELTAG_ID = "spiel_spieltag_id";

	// LOG Table - column names
	private static final String LOG_ACTIVITY = "log_activity";
	private static final String LOG_DATE = "log_date";

	// LIGA Table - column names
	private static final String LIGA_NR = "liga_nr";
	private static final String LIGA_NAME = "liganame";
	private static final String LIGA_EBENE = "ligaebene";
	private static final String LIGA_GESCHLECHT = "liga_geschlecht";
	private static final String LIGA_JUGEND = "liga_jugend";
	private static final String LIGA_SAISON = "liga_saison";
	private static final String LIGA_LINK = "liga_link";
	private static final String LIGA_POKAL = "liga_pokal";
	private static final String LIGA_INITIAL = "liga_initial";

	// SPIELTAG Table - column names
	private static final String SPIELTAG_ID = "spieltag_id";
	private static final String SPIELTAG_LIGA_NR = "spieltag_liga_nr";
	private static final String SPIELTAG_SPIELTAG_NR = "spieltag_spieltag_nr";
	private static final String SPIELTAG_SPIELTAG_NAME = "spieltag_spieltag_name";
	private static final String SPIELTAG_DATUM_BEGINN = "spieltag_datum_beginn";
	private static final String SPIELTAG_DATUM_ENDE = "spieltag_datum_ende";
	private static final String SPIELTAG_SAISON = "spieltag_saison";
	
	//HALLE Table - column names
	private static final String HALLE_NR = "halle_nr";
	private static final String HALLE_NAME = "halle_name";
	private static final String HALLE_STRASSE = "halle_strasse";
	private static final String HALLE_HAUSNUMMER = "halle_hausnummer";
	private static final String HALLE_PLZ = "halle_plz";
	private static final String HALLE_ORT = "halle_ort";

	// Table Create Statements
	private static final String CREATE_TABLE_SPIELE = "CREATE TABLE " + TABLE_SPIELE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + SPIEL_NR + " INTEGER, " + SPIEL_DATE + " TEXT, " + SPIEL_TIME + " TEXT, " + SPIEL_TEAM_HEIM + " TEXT, " + SPIEL_TEAM_GAST + " TEXT, " + SPIEL_TORE_HEIM + " INTEGER, "
			+ SPIEL_TORE_GAST + " INTEGER, " + SPIEL_PUNKTE_HEIM + " INTEGER, " + SPIEL_PUNKTE_GAST + " INTEGER, " + SPIEL_SR + " TEXT, " + SPIEL_HALLE + " INTEGER, " + SPIEL_LIGA_NR + " INTEGER, " + SPIEL_SPIELTAG_NR + " INTEGER, " + SPIEL_SPIELTAG_ID + " INTEGER, " + KEY_CREATED_AT + " TEXT" + ")";

	private static final String CREATE_TABLE_LOG = "CREATE TABLE " + TABLE_LOG + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + LOG_ACTIVITY + " TEXT, " + LOG_DATE + " TEXT, " + KEY_CREATED_AT + " TEXT" + ")";

	private static final String CREATE_TABLE_LIGA = "CREATE TABLE " + TABLE_LIGA + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + LIGA_NR + " INTEGER, " + LIGA_NAME + " TEXT, " + LIGA_EBENE + " TEXT, " + LIGA_GESCHLECHT + " TEXT, " + LIGA_JUGEND + " TEXT, " + LIGA_SAISON + " TEXT, " + LIGA_LINK
			+ " TEXT, " + LIGA_POKAL + " INTEGER, " + LIGA_INITIAL + " TEXT, " + KEY_CREATED_AT + " TEXT" + ")";

	private static final String CREATE_TABLE_SPIELTAG = "CREATE TABLE " + TABLE_SPIELTAG + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + SPIELTAG_ID + " INTEGER, " + SPIELTAG_LIGA_NR + " INTEGER, " + SPIELTAG_SPIELTAG_NR + " INTEGER, " + SPIELTAG_SPIELTAG_NAME + " TEXT, " + SPIELTAG_DATUM_BEGINN
			+ " TEXT, " + SPIELTAG_DATUM_ENDE + " TEXT, " + SPIELTAG_SAISON + " TEXT, " + KEY_CREATED_AT + " TEXT" + ")";

	private static final String CREATE_TABLE_HALLE = "CREATE TABLE " + TABLE_HALLE + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + 
			HALLE_NR + " INTEGER, " + HALLE_NAME + " TEXT, " + HALLE_STRASSE + " TEXT, " + HALLE_HAUSNUMMER + " INTEGER, " + 
			HALLE_PLZ + " TEXT, " + HALLE_ORT + " TEXT, " + KEY_CREATED_AT + " TEXT" + ")";
	
	public static DatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_SPIELE);
		db.execSQL(CREATE_TABLE_LOG);
		db.execSQL(CREATE_TABLE_LIGA);
		db.execSQL(CREATE_TABLE_SPIELTAG);
		db.execSQL(CREATE_TABLE_HALLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPIELE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPIELTAG);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HALLE);
		// create new tables
		onCreate(db);
	}

	// ADDER
	public void addSpieleForSpieltag(List<Spiel> spiele) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (Spiel spiel : spiele) {
			ContentValues values = new ContentValues();
			values.put(SPIEL_NR, spiel.getSpielNr());
			values.put(SPIEL_DATE, spiel.getDateYear() + "-" + spiel.getDateMonth() + "-" + spiel.getDateDay());
			values.put(SPIEL_TIME, spiel.getTime());
			values.put(SPIEL_TEAM_HEIM, spiel.getTeamHeim());
			values.put(SPIEL_TEAM_GAST, spiel.getTeamGast());
			values.put(SPIEL_TORE_HEIM, spiel.getToreHeim());
			values.put(SPIEL_TORE_GAST, spiel.getToreGast());
			values.put(SPIEL_PUNKTE_HEIM, spiel.getPunkteHeim());
			values.put(SPIEL_PUNKTE_GAST, spiel.getPunkteGast());
			values.put(SPIEL_SR, spiel.getSchiedsrichter());
			values.put(SPIEL_HALLE, spiel.getHalle());
			values.put(SPIEL_SPIELTAG_NR, spiel.getSpieltagsNr());
			values.put(SPIEL_LIGA_NR, spiel.getLigaNr());
			// values.put(KEY_CREATED_AT, new Date().);
			db.insert(TABLE_SPIELE, null, values);
		}
		db.close();
	}

	public long addSpieltag(Spieltag spieltag) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SPIELTAG_LIGA_NR, spieltag.getLigaNr());
		values.put(SPIELTAG_SPIELTAG_NR, spieltag.getSpieltags_Nr());
		values.put(SPIELTAG_SPIELTAG_NAME, spieltag.getSpieltags_Name());
		values.put(SPIELTAG_DATUM_BEGINN, spieltag.getDatumBeginn());
		values.put(SPIELTAG_DATUM_ENDE, spieltag.getDatumEnde());
		values.put(SPIELTAG_SAISON, spieltag.getSaison());
		// values.put(KEY_CREATED_AT, new Date().);

		// insert row
		long spiel_id = db.insert(TABLE_SPIELTAG, null, values);

		db.close();
		return spiel_id;
	}

	public long addLog(String activity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LOG_ACTIVITY, activity);
		Calendar aktuell = Calendar.getInstance();
		Log.d("date", aktuell.toString());
		// values.put(KEY_CREATED_AT, new Date().);

		// insert row
		long log_id = db.insert(TABLE_LOG, null, values);
		db.close();
		return log_id;
	}

	public long addLiga(Liga liga) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LIGA_NR, liga.getLigaNr());
		values.put(LIGA_NAME, liga.getName());
		values.put(LIGA_EBENE, liga.getEbene());
		values.put(LIGA_GESCHLECHT, liga.getGeschlecht());
		values.put(LIGA_JUGEND, liga.getJugend());
		values.put(LIGA_SAISON, liga.getSaison());
		values.put(LIGA_LINK, liga.getLink());
		values.put(LIGA_POKAL, liga.getPokal());
		values.put(LIGA_INITIAL, liga.getInitial());
		// values.put(KEY_CREATED_AT, new Date().);

		// insert row
		long liga_id = db.insert(TABLE_LIGA, null, values);
		db.close();
		return liga_id;
	}
	
	public long addHalle(Halle halle) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(HALLE_NR, halle.getHallenNr());
		values.put(HALLE_NAME, halle.getName());
		values.put(HALLE_STRASSE, halle.getStrasse());
		values.put(HALLE_HAUSNUMMER, halle.getHausnummer());
		values.put(HALLE_PLZ, halle.getPlz());
		values.put(HALLE_ORT, halle.getOrt());
		// values.put(KEY_CREATED_AT, new Date().);

		// insert row
		long halle_id = db.insert(TABLE_HALLE, null, values);
		db.close();
		return halle_id;
	}

	// ALL GETTER
	public List<Spiel> getAllGames(int ligaNr) {
		List<Spiel> ligaSpiele = new ArrayList<Spiel>();
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELE + " WHERE " + SPIEL_LIGA_NR + " = " + ligaNr;

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Spiel s = new Spiel();
				String[] tempdate = c.getString(c.getColumnIndex(SPIEL_DATE)).split("-");
				s.setDateDay(Integer.parseInt(tempdate[2]));
				s.setDateMonth(Integer.parseInt(tempdate[1]));
				s.setDateYear(Integer.parseInt(tempdate[0]));
				s.setTime(c.getString(c.getColumnIndex(SPIEL_TIME)));
				s.setSpielNr(c.getInt(c.getColumnIndex(SPIEL_NR)));
				s.setTeamHeim(c.getString(c.getColumnIndex(SPIEL_TEAM_HEIM)));
				s.setTeamGast(c.getString(c.getColumnIndex(SPIEL_TEAM_GAST)));
				s.setToreHeim(c.getInt(c.getColumnIndex(SPIEL_TORE_HEIM)));
				s.setToreGast(c.getInt(c.getColumnIndex(SPIEL_TORE_GAST)));
				s.setPunkteHeim(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_HEIM)));
				s.setPunkteGast(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_GAST)));
				s.setSchiedsrichter(c.getString(c.getColumnIndex(SPIEL_SR)));
				s.setHalle(c.getInt(c.getColumnIndex(SPIEL_HALLE)));
				s.setLigaNr(c.getInt(c.getColumnIndex(SPIEL_LIGA_NR)));
				s.setSpieltagsNr(c.getInt(c.getColumnIndex(SPIEL_SPIELTAG_NR)));

				// td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to todo list
				ligaSpiele.add(s);
			} while (c.moveToNext());
		}
		c.close();
		return ligaSpiele;
	}
	
	public Spiel getGame(int ligaNr, int spielNr){
		Spiel s = new Spiel();
		
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELE + " WHERE " + SPIEL_LIGA_NR + " = " + ligaNr +
				" AND " + SPIEL_NR + " = " + spielNr;

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c.moveToFirst()) {

			String[] tempdate = c.getString(c.getColumnIndex(SPIEL_DATE)).split("-");
			s.setDateDay(Integer.parseInt(tempdate[2]));
			s.setDateMonth(Integer.parseInt(tempdate[1]));
			s.setDateYear(Integer.parseInt(tempdate[0]));
			s.setTime(c.getString(c.getColumnIndex(SPIEL_TIME)));
			s.setSpielNr(c.getInt(c.getColumnIndex(SPIEL_NR)));
			s.setTeamHeim(c.getString(c.getColumnIndex(SPIEL_TEAM_HEIM)));
			s.setTeamGast(c.getString(c.getColumnIndex(SPIEL_TEAM_GAST)));
			s.setToreHeim(c.getInt(c.getColumnIndex(SPIEL_TORE_HEIM)));
			s.setToreGast(c.getInt(c.getColumnIndex(SPIEL_TORE_GAST)));
			s.setPunkteHeim(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_HEIM)));
			s.setPunkteGast(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_GAST)));
			s.setSchiedsrichter(c.getString(c.getColumnIndex(SPIEL_SR)));
			s.setHalle(c.getInt(c.getColumnIndex(SPIEL_HALLE)));
			s.setLigaNr(c.getInt(c.getColumnIndex(SPIEL_LIGA_NR)));
			s.setSpieltagsNr(c.getInt(c.getColumnIndex(SPIEL_SPIELTAG_NR)));

			// td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

		} else {
			Log.d(TAG, "Liga nicht gefunden!");
			return s;
		}
		c.close();
			
		return s;
	}

	public List<String> getAllLeagueTeams(int ligaNr) {
		List<String> allTeams = new ArrayList<String>();
		String selectQuery = "SELECT " + SPIEL_TEAM_HEIM + " FROM " + TABLE_SPIELE + " WHERE " + SPIEL_LIGA_NR + " = " + ligaNr + " GROUP BY " + SPIEL_TEAM_HEIM;

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				allTeams.add(c.getString(c.getColumnIndex(SPIEL_TEAM_HEIM)));

			} while (c.moveToNext());
		}
		c.close();
		return allTeams;
	}

	public List<Spiel> getAllMatchdayGames(int ligaNr, int spieltagsNr) {
		List<Spiel> ligaSpieleMatchday = new ArrayList<Spiel>();
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELE + " WHERE " + SPIEL_LIGA_NR + " = " + ligaNr + " AND " + SPIEL_SPIELTAG_NR + " = " + spieltagsNr;

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Spiel s = new Spiel();
				String[] tempdate = c.getString(c.getColumnIndex(SPIEL_DATE)).split("-");
				s.setDateDay(Integer.parseInt(tempdate[2]));
				s.setDateMonth(Integer.parseInt(tempdate[1]));
				s.setDateYear(Integer.parseInt(tempdate[0]));
				s.setTime(c.getString(c.getColumnIndex(SPIEL_TIME)));
				s.setSpielNr(c.getInt(c.getColumnIndex(SPIEL_NR)));
				s.setTeamHeim(c.getString(c.getColumnIndex(SPIEL_TEAM_HEIM)));
				s.setTeamGast(c.getString(c.getColumnIndex(SPIEL_TEAM_GAST)));
				s.setToreHeim(c.getInt(c.getColumnIndex(SPIEL_TORE_HEIM)));
				s.setToreGast(c.getInt(c.getColumnIndex(SPIEL_TORE_GAST)));
				s.setPunkteHeim(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_HEIM)));
				s.setPunkteGast(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_GAST)));
				s.setSchiedsrichter(c.getString(c.getColumnIndex(SPIEL_SR)));
				s.setHalle(c.getInt(c.getColumnIndex(SPIEL_HALLE)));
				s.setLigaNr(c.getInt(c.getColumnIndex(SPIEL_LIGA_NR)));
				s.setSpieltagsNr(c.getInt(c.getColumnIndex(SPIEL_SPIELTAG_NR)));

				// td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to todo list
				ligaSpieleMatchday.add(s);
			} while (c.moveToNext());
		}
		c.close();
		return ligaSpieleMatchday;
	}

	public List<Spiel> getAllTeamGames(int ligaNr, String teamname) {
		List<Spiel> ligaSpieleTeam = new ArrayList<Spiel>();
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELE + " WHERE " + SPIEL_TEAM_HEIM + " = '" + teamname +
        		"' AND " + SPIEL_LIGA_NR + " = " + ligaNr + " OR " + SPIEL_TEAM_GAST + " = '" + teamname +
        		"' AND " + SPIEL_LIGA_NR + " = " + ligaNr;
        		
		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Spiel s = new Spiel();
				String[] tempdate = c.getString(c.getColumnIndex(SPIEL_DATE)).split("-");
				s.setDateDay(Integer.parseInt(tempdate[2]));
				s.setDateMonth(Integer.parseInt(tempdate[1]));
				s.setDateYear(Integer.parseInt(tempdate[0]));
				s.setTime(c.getString(c.getColumnIndex(SPIEL_TIME)));
				s.setSpielNr(c.getInt(c.getColumnIndex(SPIEL_NR)));
				s.setTeamHeim(c.getString(c.getColumnIndex(SPIEL_TEAM_HEIM)));
				s.setTeamGast(c.getString(c.getColumnIndex(SPIEL_TEAM_GAST)));
				s.setToreHeim(c.getInt(c.getColumnIndex(SPIEL_TORE_HEIM)));
				s.setToreGast(c.getInt(c.getColumnIndex(SPIEL_TORE_GAST)));
				s.setPunkteHeim(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_HEIM)));
				s.setPunkteGast(c.getInt(c.getColumnIndex(SPIEL_PUNKTE_GAST)));
				s.setSchiedsrichter(c.getString(c.getColumnIndex(SPIEL_SR)));
				s.setHalle(c.getInt(c.getColumnIndex(SPIEL_HALLE)));
				s.setLigaNr(c.getInt(c.getColumnIndex(SPIEL_LIGA_NR)));
				s.setSpieltagsNr(c.getInt(c.getColumnIndex(SPIEL_SPIELTAG_NR)));

				// td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to todo list
				ligaSpieleTeam.add(s);
			} while (c.moveToNext());
		}
		c.close();
		return ligaSpieleTeam;
	}

	public List<String> getAllLogs() {
		List<String> alleLogs = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOG;

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to alleLogs list
				alleLogs.add(c.getString(c.getColumnIndex(LOG_ACTIVITY)));
			} while (c.moveToNext());
		}
		c.close();
		return alleLogs;
	}

	public List<Liga> getAlleLigen() {
		List<Liga> alleLigen = new ArrayList<Liga>();
		String selectQuery = "SELECT  * FROM " + TABLE_LIGA + " WHERE " + LIGA_INITIAL + " = 'Ja'";

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to alleLogs list
				Liga l = new Liga();
				l.setLigaNr(c.getInt(c.getColumnIndex(LIGA_NR)));
				l.setName(c.getString(c.getColumnIndex(LIGA_NAME)));
				l.setEbene(c.getString(c.getColumnIndex(LIGA_EBENE)));
				l.setGeschlecht(c.getString(c.getColumnIndex(LIGA_GESCHLECHT)));
				l.setJugend(c.getString(c.getColumnIndex(LIGA_JUGEND)));
				l.setSaison(c.getString(c.getColumnIndex(LIGA_JUGEND)));
				l.setLink(c.getString(c.getColumnIndex(LIGA_LINK)));
				l.setPokal(c.getInt(c.getColumnIndex(LIGA_POKAL)));
				l.setInitial(c.getString(c.getColumnIndex(LIGA_INITIAL)));

				alleLigen.add(l);
			} while (c.moveToNext());
		}
		c.close();
		return alleLigen;
	}

	public List<Liga> getAlleLigenNochNichtVorhanden() {
		List<Liga> alleLigenNichtVorhanden = new ArrayList<Liga>();
		String selectQuery = "SELECT  * FROM " + TABLE_LIGA + " WHERE " + LIGA_INITIAL + " = 'Nein'";

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to alleLogs list
				Liga l = new Liga();
				l.setLigaNr(c.getInt(c.getColumnIndex(LIGA_NR)));
				l.setName(c.getString(c.getColumnIndex(LIGA_NAME)));
				l.setEbene(c.getString(c.getColumnIndex(LIGA_EBENE)));
				l.setGeschlecht(c.getString(c.getColumnIndex(LIGA_GESCHLECHT)));
				l.setJugend(c.getString(c.getColumnIndex(LIGA_JUGEND)));
				l.setSaison(c.getString(c.getColumnIndex(LIGA_JUGEND)));
				l.setLink(c.getString(c.getColumnIndex(LIGA_LINK)));
				l.setPokal(c.getInt(c.getColumnIndex(LIGA_POKAL)));
				l.setInitial(c.getString(c.getColumnIndex(LIGA_INITIAL)));

				alleLigenNichtVorhanden.add(l);
			} while (c.moveToNext());
		}
		c.close();
		return alleLigenNichtVorhanden;
	}

	public List<Spieltag> getAllSpieltageForLiga(int ligaNr) {
		List<Spieltag> ligaSpieltage = new ArrayList<Spieltag>();
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELTAG + " WHERE " + SPIELTAG_LIGA_NR + " = " + ligaNr;

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Spieltag s = new Spieltag();
				s.setSpieltags_ID(c.getInt(c.getColumnIndex(SPIELTAG_ID)));
				s.setLigaNr(c.getInt(c.getColumnIndex(SPIELTAG_LIGA_NR)));
				s.setSpieltags_Nr(c.getInt(c.getColumnIndex(SPIELTAG_SPIELTAG_NR)));
				s.setSpieltags_Name(c.getString(c.getColumnIndex(SPIELTAG_SPIELTAG_NAME)));
				s.setDatumBeginn(c.getString(c.getColumnIndex(SPIELTAG_DATUM_BEGINN)));
				s.setDatumEnde(c.getString(c.getColumnIndex(SPIELTAG_DATUM_ENDE)));
				s.setSaison(c.getString(c.getColumnIndex(SPIELTAG_SAISON)));
				// td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to todo list
				ligaSpieltage.add(s);
			} while (c.moveToNext());
		}
		c.close();
		return ligaSpieltage;
	}

	// TODO: Keine Cursor weitergeben!
	public Cursor getAllPlayedGames(int ligaNr) {
		List<Spiel> ligaSpiele = new ArrayList<Spiel>();
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELE + " WHERE " + SPIEL_LIGA_NR + " = " + ligaNr + " AND " + SPIEL_TORE_HEIM + " != 0";

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		c.close();
		return c;
	}

	// GETTER
	public Liga getLiga(int ligaNr) {
		String selectQuery = "SELECT * FROM " + TABLE_LIGA + " WHERE " + LIGA_NR + " = " + ligaNr;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		Liga l = new Liga();
		// looping through all rows and adding to list
		if (c.moveToFirst()) {

			l.setLigaNr(c.getInt(c.getColumnIndex(LIGA_NR)));
			l.setName(c.getString(c.getColumnIndex(LIGA_NAME)));
			l.setEbene(c.getString(c.getColumnIndex(LIGA_EBENE)));
			l.setGeschlecht(c.getString(c.getColumnIndex(LIGA_GESCHLECHT)));
			l.setJugend(c.getString(c.getColumnIndex(LIGA_JUGEND)));
			l.setSaison(c.getString(c.getColumnIndex(LIGA_SAISON)));
			l.setLink(c.getString(c.getColumnIndex(LIGA_LINK)));
			l.setPokal(c.getInt(c.getColumnIndex(LIGA_POKAL)));
			l.setInitial(c.getString(c.getColumnIndex(LIGA_INITIAL)));

		} else {
			Log.d(TAG, "Liga nicht gefunden!");
			return l;
		}
		c.close();
		return l;
	}
	
	public Halle getHalle(int hallenNr) {
		String selectQuery = "SELECT * FROM " + TABLE_HALLE + " WHERE " + HALLE_NR + " = " + hallenNr;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		Halle h = new Halle();;
		// looping through all rows and adding to list
		if (c.moveToFirst()) {

			h.setHallenNr(c.getInt(c.getColumnIndex(HALLE_NR)));
			h.setName(c.getString(c.getColumnIndex(HALLE_NAME)));
			h.setStrasse(c.getString(c.getColumnIndex(HALLE_STRASSE)));
			h.setHausnummer(c.getInt(c.getColumnIndex(HALLE_HAUSNUMMER)));
			h.setPlz(c.getString(c.getColumnIndex(HALLE_PLZ)));
			h.setOrt(c.getString(c.getColumnIndex(HALLE_ORT)));

		} else {
			Log.d(TAG, "Halle nicht gefunden!");
			return h;
		}
		c.close();
		return h;
	}
	
	public List<Halle> getAlleHallen(){
		List<Halle> alleHallen = new ArrayList<Halle>();
		String selectQuery = "SELECT * FROM " + TABLE_HALLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c.moveToFirst()){
			do{
				Halle h = new Halle();
				h.setHallenNr(c.getInt(c.getColumnIndex(HALLE_NR)));
				h.setName(c.getString(c.getColumnIndex(HALLE_NAME)));
				h.setStrasse(c.getString(c.getColumnIndex(HALLE_STRASSE)));
				h.setHausnummer(c.getInt(c.getColumnIndex(HALLE_HAUSNUMMER)));
				h.setPlz(c.getString(c.getColumnIndex(HALLE_PLZ)));
				h.setOrt(c.getString(c.getColumnIndex(HALLE_ORT)));
				alleHallen.add(h);
			}while(c.moveToNext());
		}
		c.close();
		return alleHallen;
	}

	public List<String> getTabelle(int ligaNr) {
		List<String> tabelle = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELE + " WHERE " + SPIEL_LIGA_NR + " = " + ligaNr + " AND " + SPIEL_TORE_HEIM + " != 0";

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				String spiel = c.getString(c.getColumnIndex(SPIEL_TEAM_HEIM)) + " gegen " + c.getString(c.getColumnIndex(SPIEL_TEAM_GAST)) + " | " + c.getInt(c.getColumnIndex(SPIEL_TORE_HEIM)) + " : " + c.getInt(c.getColumnIndex(SPIEL_TORE_GAST));

				// td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to todo list
				tabelle.add(spiel);
				Log.d("tabelle", spiel);
			} while (c.moveToNext());
		}
		c.close();
		return tabelle;
	}

	// CHECKER
	public boolean gameAlreadyExists(Spiel spiel) {
		String selectQuery = "SELECT  * FROM " + TABLE_SPIELE + " WHERE " + SPIEL_LIGA_NR + " = " + spiel.getLigaNr() + " AND " + SPIEL_NR + " = " + spiel.getSpielNr();

		Log.d(TAG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.getCount() == 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}

	}

	// UPDATER
	public void updateLigaInitial(Liga liga) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "UPDATE " + TABLE_LIGA + " SET " + LIGA_INITIAL + " = '" + liga.getInitial() + "' WHERE " + LIGA_NR + " = " + liga.getLigaNr();
		db.execSQL(sql);
		db.close();
	}

	@Override
	public synchronized void close() {
		if (sInstance != null)
			db.close();
	}
}
