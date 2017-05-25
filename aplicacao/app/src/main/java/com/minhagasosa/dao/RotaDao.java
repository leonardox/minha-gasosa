package com.minhagasosa.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table "ROTA".
 */
public class RotaDao extends AbstractDao<Rota, Long> {

    public static final String TABLENAME = "ROTA";
    private final int THREE = 3;
    private final int FOUR = 4;
    private final int FIVE = 5;
    private final int SIX = 6;
    private final int SEVEN = 7;
    private final int EIGHT = 8;
    private final int NINE = 9;




    public RotaDao(DaoConfig config) {
        super(config);
    }

    public RotaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ROTA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NOME\" TEXT," + // 1: NOME
                "\"IDA_EVOLTA\" INTEGER," + // 2: idaEVolta
                "\"DISTANCIA_IDA\" REAL," + // THREE: distanciaIda
                "\"DISTANCIA_VOLTA\" REAL," + // FOUR: distanciaVolta
                "\"REPETE_SEMANA\" INTEGER," + // FIVE: repeteSemana
                "\"REPETICOES\" INTEGER," + // SIX: repetoicoes
                "\"DE_ROTINA\" INTEGER," + // SEVEN: deRotina
                "\"DATA\" INTEGER);"); //EIGHT: data
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ROTA\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected final void bindValues(SQLiteStatement stmt, Rota entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String Nome = entity.getNome();
        if (Nome != null) {
            stmt.bindString(2, Nome);
        }

        Boolean idaEVolta = entity.getIdaEVolta();
        if (idaEVolta != null) {
            stmt.bindLong(THREE, idaEVolta ? 1L : 0L);
        }

        Float distanciaIda = entity.getDistanciaIda();
        if (distanciaIda != null) {
            stmt.bindDouble(FOUR, distanciaIda);
        }

        Float distanciaVolta = entity.getDistanciaVolta();
        if (distanciaVolta != null) {
            stmt.bindDouble(FIVE, distanciaVolta);
        }

        Boolean repeteSemana = entity.getRepeteSemana();
        if (repeteSemana != null) {
            stmt.bindLong(SIX, repeteSemana ? 1L : 0L);
        }

        Integer repetoicoes = entity.getRepetoicoes();
        if (repetoicoes != null) {
            stmt.bindLong(SEVEN, repetoicoes);
        }

        Boolean deRotina = entity.getDeRotina();
        if (deRotina != null) {
            stmt.bindLong(EIGHT, deRotina ? 1L: 0L);
        }
        long data = entity.getDataInLong();
        if (data != -1) {
            stmt.bindLong(NINE, data);
        }
    }

    /** @inheritdoc */
    @Override
    public final Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public final Rota readEntity(Cursor cursor, int offset) {
        Rota entity = new Rota( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // NOME
                cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // idaEVolta
                cursor.isNull(offset + THREE) ? null : cursor.getFloat(offset + THREE), // distanciaIda
                cursor.isNull(offset + FOUR) ? null : cursor.getFloat(offset + FOUR), // distanciaVolta
                cursor.isNull(offset + FIVE) ? null : cursor.getShort(offset + FIVE) != 0, // repeteSemana
                cursor.isNull(offset + SIX) ? null : cursor.getInt(offset + SIX), // repetoicoes
                cursor.isNull(offset + SEVEN) ? null : cursor.getShort(offset + SEVEN) != 0, // deRotina
                cursor.isNull(offset + EIGHT) ? null : cursor.getLong(offset + EIGHT)// deRotina
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public final void readEntity(Cursor cursor, Rota entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNome(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIdaEVolta(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setDistanciaIda(cursor.isNull(offset + THREE) ? null : cursor.getFloat(offset + THREE));
        entity.setDistanciaVolta(cursor.isNull(offset + FOUR) ? null : cursor.getFloat(offset + FOUR));
        entity.setRepeteSemana(cursor.isNull(offset + FIVE) ? null : cursor.getShort(offset + FIVE) != 0);
        entity.setRepetoicoes(cursor.isNull(offset + SIX) ? null : cursor.getInt(offset + SIX));
        entity.setDeRotina(cursor.isNull(offset + SEVEN) ? null : cursor.getShort(offset + SEVEN) != 0);
        entity.setData(cursor.getLong(offset + EIGHT));
    }

    /** @inheritdoc */
    @Override
    protected final Long updateKeyAfterInsert(Rota entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public final Long getKey(Rota entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

    /**
     * Properties of entity Rota.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "id", true, "_id");
        public final static Property NOME = new Property(1, String.class, "NOME", false, "NOME");
        public final static Property IDA_E_VOLTA = new Property(2, Boolean.class, "idaEVolta", false, "IDA_EVOLTA");
        public final static Property DISTANCIA_IDA = new Property(3, Float.class, "distanciaIda", false, "DISTANCIA_IDA");
        public final static Property DISTANCIA_VOLTA = new Property(4, Float.class, "distanciaVolta", false, "DISTANCIA_VOLTA");
        public final static Property REPETE_SEMANA = new Property(5, Boolean.class, "repeteSemana", false, "REPETE_SEMANA");
        public final static Property REPETICOES = new Property(6, Integer.class, "repetoicoes", false, "REPETICOES");
        public final static Property DE_ROTINA = new Property(7, Boolean.class, "deRotina", false, "DE_ROTINA");
        public final static Property DATA = new Property(8, Long.class, "data", false, "DATA");
    }
}
