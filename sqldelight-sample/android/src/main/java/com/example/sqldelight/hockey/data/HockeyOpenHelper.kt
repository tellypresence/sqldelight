package com.example.sqldelight.hockey.data

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import android.content.Context
import com.example.sqldelight.hockey.QueryWrapper
import com.example.sqldelight.hockey.data.PlayerVals.Position
import com.example.sqldelight.hockey.data.PlayerVals.Shoots
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.AndroidSqlDatabase
import com.squareup.sqldelight.db.SqlDatabase

object HockeyOpenHelper : SupportSQLiteOpenHelper.Callback(QueryWrapper.version) {
  private var instance: QueryWrapper? = null

  private fun createQueryWrapper(database: SqlDatabase): QueryWrapper {
    return QueryWrapper(
        database = database,
        playerAdapter = Player.Adapter(
            shootsAdapter = EnumColumnAdapter(),
            positionAdapter = EnumColumnAdapter()
        )
    )
  }

  fun getInstance(context: Context): QueryWrapper {
    if (instance == null) {
      instance = createQueryWrapper(AndroidSqlDatabase(
          helper = QueryWrapper.Helper,
          context = context,
          callback = this
      ))
    }
    return instance!!
  }

  override fun onCreate(db: SupportSQLiteDatabase) {
    val sqlDatabase = AndroidSqlDatabase(db)
    val queryWrapper = createQueryWrapper(sqlDatabase)

    QueryWrapper.onCreate(sqlDatabase.getConnection())

    // Populate initial data.
    val ducks = queryWrapper.teamQueries.insertTeam(
        "Anaheim Ducks", "Randy Carlyle", true)

    queryWrapper.playerQueries.insertPlayer(
        "Corey", "Perry", 10, ducks, 30, 210F,
        Shoots.RIGHT, Position.RIGHT_WING
    )

    val getzlaf = queryWrapper.playerQueries.insertPlayer(
        "Ryan", "Getzlaf", 15, ducks, 30, 221F,
        Shoots.RIGHT, Position.CENTER
    )
    queryWrapper.teamQueries.updateCaptain(getzlaf, ducks)

    val pens = queryWrapper.teamQueries.insertTeam(
        "Pittsburgh Penguins", "Mike Sullivan", true)

    val crosby = queryWrapper.playerQueries.insertPlayer(
        "Sidney", "Crosby", 87, pens, 28, 200F,
        Shoots.LEFT, Position.CENTER
    )
    queryWrapper.teamQueries.updateCaptain(crosby, pens)

    val sharks = queryWrapper.teamQueries.insertTeam(
        "San Jose Sharks", "Peter DeBoer", false)

    queryWrapper.playerQueries.insertPlayer(
        "Patrick", "Marleau", 12, sharks, 36, 220F,
        Shoots.LEFT, Position.LEFT_WING
    )

    val pavelski = queryWrapper.playerQueries.insertPlayer(
        "Joe", "Pavelski", 8, sharks, 31, 194F,
        Shoots.RIGHT, Position.CENTER
    )
    queryWrapper.teamQueries.updateCaptain(pavelski, sharks)
  }

  override fun onOpen(db: SupportSQLiteDatabase) {
    super.onOpen(db)
    db.execSQL("PRAGMA foreign_keys=ON")
  }

  override fun onUpgrade(
    db: SupportSQLiteDatabase,
    oldVersion: Int,
    newVersion: Int
  ) {
    QueryWrapper.onMigrate(AndroidSqlDatabase(db).getConnection(), oldVersion, newVersion)
  }

}
