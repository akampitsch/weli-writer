package com.szoldapps.weli.writer.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.szoldapps.weli.writer.data.db.entity.GameEntity
import com.szoldapps.weli.writer.data.db.entity.GameWithPlayersEntity
import com.szoldapps.weli.writer.data.db.entity.PlayerEntity
import com.szoldapps.weli.writer.data.db.entity.PlayerGameEntity
import com.szoldapps.weli.writer.data.db.mapper.mapToGameEntity
import com.szoldapps.weli.writer.data.db.mapper.mapToPlayerEntities
import com.szoldapps.weli.writer.domain.Game

@Dao
interface PlayerGameDao {

    @Transaction
    @Query("SELECT * FROM game WHERE game_match_id=:matchId")
    fun getGamesWithPlayersEntities(matchId: Long): LiveData<List<GameWithPlayersEntity>>

    @Insert
    fun insertGameEntity(gameEntity: GameEntity): Long

    @Insert
    fun insertPlayerEntities(playerEntities: List<PlayerEntity>): List<Long>

    @Insert
    fun insert(playerGameEntity: List<PlayerGameEntity>)

    @Transaction
    fun insert(game: Game, matchId: Long) {
        val gameId = insertGameEntity(game.mapToGameEntity(matchId))
        val playerIds = insertPlayerEntities(game.players.mapToPlayerEntities())
        val playerGameCrossRefs = playerIds.map { playerId ->
            PlayerGameEntity(
                playerId = playerId,
                gameId = gameId
            )
        }
        insert(playerGameCrossRefs)
    }
}
