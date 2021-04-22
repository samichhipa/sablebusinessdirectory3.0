package com.macinternetservices.sablebusinessdirectory.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.macinternetservices.sablebusinessdirectory.viewobject.ItemCategory;

import java.util.List;

@Dao
public interface ItemCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemCategory itemCategory);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ItemCategory itemCategory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItemCategory> cityCategories);

    @Query("DELETE FROM ItemCategory")
    void deleteAllCityCategory();

    @Query("DELETE FROM ItemCategory WHERE id = :id")
    void deleteCityCategoryById(String id);

    @Query("SELECT max(sorting) from ItemCategory ")
    int getMaxSortingByValue();

    @Query("SELECT * FROM ItemCategory WHERE cityId = :id ORDER BY sorting")
    LiveData<List<ItemCategory>> getAllCityCategoryById(String id);

    @Query("DELETE FROM ItemCategory WHERE id =:id")
    public abstract void deleteItemCategoryById(String id);



}
