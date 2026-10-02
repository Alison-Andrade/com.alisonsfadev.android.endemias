package com.alisonsfadev.endemias.features.visitas.di

import com.alisonsfadev.endemias.features.visitas.data.VisitaTratamentoRepositoryImpl
import com.alisonsfadev.endemias.features.visitas.domain.VisitaTratamentoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TratamentoBindsModule {
    @Binds
    @Singleton
    abstract fun bindRepository(impl: VisitaTratamentoRepositoryImpl): VisitaTratamentoRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ClockModule {
    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.systemDefaultZone()
}