package com.nativeplayer.data.repository;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile FavoriteDao _favoriteDao;

  private volatile WatchHistoryDao _watchHistoryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `itemId` TEXT NOT NULL, `title` TEXT NOT NULL, `subtitle` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `streamUrl` TEXT NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `watch_history` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `itemId` TEXT NOT NULL, `title` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `streamUrl` TEXT NOT NULL, `position` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `seasonNumber` INTEGER NOT NULL, `episodeNumber` INTEGER NOT NULL, `episodeTitle` TEXT NOT NULL, `lastWatchedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '188ff60e44c5d6eb035ee9094dc897c1')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `favorites`");
        db.execSQL("DROP TABLE IF EXISTS `watch_history`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFavorites = new HashMap<String, TableInfo.Column>(8);
        _columnsFavorites.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("itemId", new TableInfo.Column("itemId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("subtitle", new TableInfo.Column("subtitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("streamUrl", new TableInfo.Column("streamUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("addedAt", new TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavorites = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavorites = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavorites = new TableInfo("favorites", _columnsFavorites, _foreignKeysFavorites, _indicesFavorites);
        final TableInfo _existingFavorites = TableInfo.read(db, "favorites");
        if (!_infoFavorites.equals(_existingFavorites)) {
          return new RoomOpenHelper.ValidationResult(false, "favorites(com.nativeplayer.data.model.FavoriteEntity).\n"
                  + " Expected:\n" + _infoFavorites + "\n"
                  + " Found:\n" + _existingFavorites);
        }
        final HashMap<String, TableInfo.Column> _columnsWatchHistory = new HashMap<String, TableInfo.Column>(12);
        _columnsWatchHistory.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("itemId", new TableInfo.Column("itemId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("streamUrl", new TableInfo.Column("streamUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("seasonNumber", new TableInfo.Column("seasonNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("episodeNumber", new TableInfo.Column("episodeNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("episodeTitle", new TableInfo.Column("episodeTitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWatchHistory.put("lastWatchedAt", new TableInfo.Column("lastWatchedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWatchHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWatchHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWatchHistory = new TableInfo("watch_history", _columnsWatchHistory, _foreignKeysWatchHistory, _indicesWatchHistory);
        final TableInfo _existingWatchHistory = TableInfo.read(db, "watch_history");
        if (!_infoWatchHistory.equals(_existingWatchHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "watch_history(com.nativeplayer.data.model.WatchHistoryEntity).\n"
                  + " Expected:\n" + _infoWatchHistory + "\n"
                  + " Found:\n" + _existingWatchHistory);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "188ff60e44c5d6eb035ee9094dc897c1", "326fe9ddc5bbbb06782505f7411dd5a2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorites","watch_history");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `favorites`");
      _db.execSQL("DELETE FROM `watch_history`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FavoriteDao.class, FavoriteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WatchHistoryDao.class, WatchHistoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FavoriteDao favoriteDao() {
    if (_favoriteDao != null) {
      return _favoriteDao;
    } else {
      synchronized(this) {
        if(_favoriteDao == null) {
          _favoriteDao = new FavoriteDao_Impl(this);
        }
        return _favoriteDao;
      }
    }
  }

  @Override
  public WatchHistoryDao watchHistoryDao() {
    if (_watchHistoryDao != null) {
      return _watchHistoryDao;
    } else {
      synchronized(this) {
        if(_watchHistoryDao == null) {
          _watchHistoryDao = new WatchHistoryDao_Impl(this);
        }
        return _watchHistoryDao;
      }
    }
  }
}
