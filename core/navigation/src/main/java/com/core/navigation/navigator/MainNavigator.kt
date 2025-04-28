package com.core.navigation.navigator

import kotlinx.serialization.Serializable

interface MainNavigator : Navigator

@Serializable
data object Etc

@Serializable
data object Terms

@Serializable
data object ScrapPolicy
