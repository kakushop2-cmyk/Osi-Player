package com.nativeplayer.data.repository;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nativeplayer.data.model.WatchHistoryEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class WatchHistoryDao_Impl implements WatchHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WatchHistoryEntity> __insertionAdapterOfWatchHistoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByItemId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public WatchHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWatchHistoryEntity = new EntityInsertionAdapter<WatchHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `watch_history` (`id`,`type`,`itemId`,`title`,`imageUrl`,`streamUrl`,`position`,`duration`,`seasonNumber`,`episodeNumber`,`episodeTitle`,`lastWatchedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WatchHistoryEntity entity) {
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
        if (entity.getImageUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getImageUrl());
        }
        if (entity.getStreamUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStreamUrl());
        }
        statement.bindLong(7, entity.getPosition());
        statement.bindLong(8, entity.getDuration());
        statement.bindLong(9, entity.getSeasonNumber());
        statement.bindLong(10, entity.getEpisodeNumber());
        if (entity.getEpisodeTitle() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getEpisodeTitle());
        }
        statement.bindLong(12, entity.getLastWatchedAt());
      }
    };
    this.__preparedStmtOfDeleteByItemId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM watch_history WHERE itemId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM watch_history";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final WatchHistoryEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWatchHistoryEntity.insert(entry);
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
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WatchHistoryEntity>> getAll() {
    final String _sql = "SELECT * FROM watch_history ORDER BY lastWatchedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"watch_history"}, new Callable<List<WatchHistoryEntity>>() {
      @Override
      @NonNull
      public List<WatchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "itemId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfLastWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastWatchedAt");
          final List<WatchHistoryEntity> _result = new ArrayList<WatchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WatchHistoryEntity _item;
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
            final long _tmpPosition;
            _tmpPosition = _cursor.getLong(_cursorIndexOfPosition);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final int _tmpSeasonNumber;
            _tmpSeasonNumber = _cursor.getInt(_cursorIndexOfSeasonNumber);
            final int _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpLastWatchedAt;
            _tmpLastWatchedAt = _cursor.getLong(_cursorIndexOfLastWatchedAt);
            _item = new WatchHistoryEntity(_tmpId,_tmpType,_tmpItemId,_tmpTitle,_tmpImageUrl,_tmpStreamUrl,_tmpPosition,_tmpDuration,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpLastWatchedAt);
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
  public Flow<List<WatchHistoryEntity>> getRecent(final int limit) {
    final String _sql = "SELECT * FROM watch_history WHERE type != 'live' ORDER BY lastWatchedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"watch_history"}, new Callable<List<WatchHistoryEntity>>() {
      @Override
      @NonNull
      public List<WatchHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "itemId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfLastWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastWatchedAt");
          final List<WatchHistoryEntity> _result = new ArrayList<WatchHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WatchHistoryEntity _item;
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
            final long _tmpPosition;
            _tmpPosition = _cursor.getLong(_cursorIndexOfPosition);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final int _tmpSeasonNumber;
            _tmpSeasonNumber = _cursor.getInt(_cursorIndexOfSeasonNumber);
            final int _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpLastWatchedAt;
            _tmpLastWatchedAt = _cursor.getLong(_cursorIndexOfLastWatchedAt);
            _item = new WatchHistoryEntity(_tmpId,_tmpType,_tmpItemId,_tmpTitle,_tmpImageUrl,_tmpStreamUrl,_tmpPosition,_tmpDuration,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpLastWatchedAt);
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
  public Object getByItemId(final String itemId,
      final Continuation<? super WatchHistoryEntity> $completion) {
    final String _sql = "SELECT * FROM watch_history WHERE itemId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itemId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, itemId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WatchHistoryEntity>() {
      @Override
      @Nullable
      public WatchHistoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "itemId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfEpisodeNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeNumber");
          final int _cursorIndexOfEpisodeTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "episodeTitle");
          final int _cursorIndexOfLastWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastWatchedAt");
          final WatchHistoryEntity _result;
          if (_cursor.moveToFirst()) {
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
            final long _tmpPosition;
            _tmpPosition = _cursor.getLong(_cursorIndexOfPosition);
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final int _tmpSeasonNumber;
            _tmpSeasonNumber = _cursor.getInt(_cursorIndexOfSeasonNumber);
            final int _tmpEpisodeNumber;
            _tmpEpisodeNumber = _cursor.getInt(_cursorIndexOfEpisodeNumber);
            final String _tmpEpisodeTitle;
            if (_cursor.isNull(_cursorIndexOfEpisodeTitle)) {
              _tmpEpisodeTitle = null;
            } else {
              _tmpEpisodeTitle = _cursor.getString(_cursorIndexOfEpisodeTitle);
            }
            final long _tmpLastWatchedAt;
            _tmpLastWatchedAt = _cursor.getLong(_cursorIndexOfLastWatchedAt);
            _result = new WatchHistoryEntity(_tmpId,_tmpType,_tmpItemId,_tmpTitle,_tmpImageUrl,_tmpStreamUrl,_tmpPosition,_tmpDuration,_tmpSeasonNumber,_tmpEpisodeNumber,_tmpEpisodeTitle,_tmpLastWatchedAt);
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
