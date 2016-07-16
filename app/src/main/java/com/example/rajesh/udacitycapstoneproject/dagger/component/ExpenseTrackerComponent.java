package com.example.rajesh.udacitycapstoneproject.dagger.component;


import com.example.rajesh.udacitycapstoneproject.dagger.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {NetworkModule.class})
public interface ExpenseTrackerComponent {

}
