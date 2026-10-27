package com.jacqulin.taskmanager.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jacqulin.taskmanager.core.database.TaskManagerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE tasks ADD COLUMN dayPlanOrder INTEGER")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): TaskManagerDatabase = Room.databaseBuilder(
        context,
        TaskManagerDatabase::class.java,
        "task_manager.db",
    )
        .addMigrations(migration1To2)
        .fallbackToDestructiveMigration(true)
        .build()
}
