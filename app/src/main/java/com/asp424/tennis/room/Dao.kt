package com.asp424.tennis.room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): MutableList<User>


    @Query("SELECT * FROM userbig")
    fun getAllUsersBig(): MutableList<UserBig>


    @Query("SELECT * FROM trainermod")
    fun getAllTrainers(): MutableList<TrainerMod>

    @Query("SELECT EXISTS (SELECT * FROM user WHERE uid = :id)")
    fun existsUsersList(id: String): Boolean

    @Query("SELECT EXISTS (SELECT * FROM userbig WHERE uid = :id)")
    fun existsUsersBig(id: String): Boolean


    @Query("SELECT EXISTS (SELECT * FROM trainermod WHERE uid = :id)")
    fun existsTrainersList(id: String): Boolean

    @Query("SELECT * FROM user WHERE uid = :id")
    fun loadById(id: String): User

    @Query("SELECT * FROM userbig WHERE uid = :id")
    fun loadUserBigById(id: String): UserBig

    @Query("SELECT * FROM user WHERE color = :color")
    fun loadByColor(color: String): User

    @Query("SELECT * FROM userbig WHERE color = :color")
    fun loadByColorUserBig(color: String): UserBig

    @Query(
        "SELECT * FROM user WHERE name_child LIKE :first AND " +
                "secondName_child LIKE :last LIMIT 1"
    )
    fun findByName(first: String, last: String): User

    @Update
    fun updateAllUsers(vararg users: User)

    @Update
    fun updateAllUsersBig(vararg users: UserBig)

    @Update
    fun updateAllTrainers(vararg trainers: TrainerMod)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsersBig(vararg users: UserBig)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTrainers(vararg trainers: TrainerMod)

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM user WHERE uid = :id")
    fun delete(id: String)

    @Query("DELETE FROM userbig WHERE uid = :id")
    fun deleteBig(id: String)

    @Delete
    fun deleteUser(user: User)

    @Delete
    fun deleteUserBig(user: UserBig)

    @Query("DELETE FROM TrainerMod")
    fun deleteAllTrainers()

    @Query("DELETE FROM User")
    fun deleteAllUsers()

    @Query("DELETE FROM UserBig")
    fun deleteAllUsersBig()

    @Query("SELECT COUNT(*) FROM user")
    fun getCount(): LiveData<Int?>?

    @Query("SELECT EXISTS (SELECT * FROM eventmodelroom WHERE uid = :id)")
    fun existsEventList(id: String): Boolean

    @Query("DELETE FROM eventmodelroom WHERE uid = :id")
    fun deleteEvent(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEvents(vararg events: EventModelRoom)

    @Query("SELECT * FROM eventmodelroom")
    fun getAllEvents(): List<EventModelRoom>

    @Insert
    fun insertEvent(event: EventModelRoom)


}