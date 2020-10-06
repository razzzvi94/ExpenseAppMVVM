package com.example.expenseappmvvm.data.rx

import io.reactivex.Scheduler

interface RxSchedulers {

    fun androidUI(): Scheduler

    fun io(): Scheduler

    fun computation(): Scheduler

    fun network(): Scheduler

    fun immediate(): Scheduler

    fun background(): Scheduler
}