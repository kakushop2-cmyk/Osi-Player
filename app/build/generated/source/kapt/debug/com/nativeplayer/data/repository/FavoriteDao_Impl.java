package com.nativeplayer.data.repository;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nativeplayer.data.model.FavoriteEntity;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FavoriteDao_Impl implements FavoriteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FavoriteEntity> __insertionAdapterOfFavoriteEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByItemId;

  public FavoriteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFavoriteEntity = new EntityInsertionAdapter<FavoriteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `favorites` (`id`,`type`,`itemId`,`title`,`subtitle`,`imageUrl`,`streamUrl`,`addedAt`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getType());
        }
        if (entity.getItemId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getItemId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTitle());
        }
        if (entity.getSubtitle() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSubtitle());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImageUrl());
        }
        if (entity.getStreamUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getStreamUrl());
        }
        statement.bindLong(8, entity.getAddedAt());
      }
    };
    this.__preparedStmtOfDeleteByItemId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM favorites WHERE itemId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FavoriteEntity favorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFavoriteEntity.insert(favorite);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByItemId(final String itemId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByItemId.acquire();
        int _argIndex = 1;
        if (itemId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, itemId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByItemId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FavoriteEntity>> getAll() {
    final String _sql = "SELECT * FROM favorites ORDER BY addedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"favorites"}, new Callable<List<FavoriteEntity>>() {
      @Override
      @NonNull
      public List<FavoriteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "itemId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSubtitle = CursorUtil.getColumnIndexOrThrow(_cursor, "subtitle");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final List<FavoriteEntity> _result = new ArrayList<FavoriteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FavoriteEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpItemId;
            if (_cursor.isNull(_cursorIndexOfItemId)) {
              _tmpItemId = null;
            } else {
              _tmpItemId = _cursor.getString(_cursorIndexOfItemId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpSubtitle;
            if (_cursor.isNull(_cursorIndexOfSubtitle)) {
              _tmpSubtitle = null;
            } else {
              _tmpSubtitle = _cursor.getString(_cursorIndexOfSubtitle);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpStreamUrl;
            if (_cursor.isNull(_cursorIndexOfStreamUrl)) {
              _tmpStreamUrl = null;
            } else {
              _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            }
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _item = new FavoriteEntity(_tmpId,_tmpType,_tmpItemId,_tmpTitle,_tmpSubtitle,_tmpImageUrl,_tmpStreamUrl,_tmpAddedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<FavoriteEntity>> getByType(final String type) {
    final String _sql = "SELECT * FROM favorites WHERE type = ? ORDER BY addedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"favorites"}, new Callable<List<FavoriteEntity>>() {
      @Override
      @NonNull
      public List<FavoriteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "itemId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSubtitle = CursorUtil.getColumnIndexOrThrow(_cursor, "subtitle");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final List<FavoriteEntity> _result = new ArrayList<FavoriteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FavoriteEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpItemId;
            if (_cursor.isNull(_cursorIndexOfItemId)) {
              _tmpItemId = null;
            } else {
              _tmpItemId = _cursor.getString(_cursorIndexOfItemId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpSubtitle;
            if (_cursor.isNull(_cursorIndexOfSubtitle)) {
              _tmpSubtitle = null;
            } else {
              _tmpSubtitle = _cursor.getString(_cursorIndexOfSubtitle);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpStreamUrl;
            if (_cursor.isNull(_cursorIndexOfStreamUrl)) {
              _tmpStreamUrl = null;
            } else {
              _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            }
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _item = new FavoriteEntity(_tmpId,_tmpType,_tmpItemId,_tmpTitle,_tmpSubtitle,_tmpImageUrl,_tmpStreamUrl,_tmpAddedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Boolean> isFavorite(final String itemId) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itemId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, itemId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"favorites"}, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object isFavoriteSync(final String itemId,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itemId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, itemId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
