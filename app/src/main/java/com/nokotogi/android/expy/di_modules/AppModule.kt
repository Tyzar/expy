package com.nokotogi.android.expy.di_modules

import android.content.Context
import com.nokotogi.android.expy.data.db.room.AppRoomDb
import com.nokotogi.android.expy.data.db.room.buildAppRoomDb
import com.nokotogi.android.expy.data.repositories.CategoryRepository
import com.nokotogi.android.expy.data.repositories.ExpenseRepository
import com.nokotogi.android.expy.domain.repositories.ICategoryRepository
import com.nokotogi.android.expy.domain.repositories.IExpenseRepository
import com.nokotogi.android.expy.data.repositories.dummies.DummyCategoryRepo
import com.nokotogi.android.expy.data.repositories.dummies.DummyExpenseRepo
import com.nokotogi.android.expy.domain.models.ExpenseCategory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    companion object {
        @Provides
        @Singleton
        fun provideAppDb(@ApplicationContext context: Context): AppRoomDb {
            return buildAppRoomDb(context)
        }
    }

    @Binds
    abstract fun provideCategoryRepo(categoryRepository: CategoryRepository): ICategoryRepository

    @Binds
    abstract fun provideExpenseRepo(expenseRepository: ExpenseRepository): IExpenseRepository
}