package com.carlostorres.wordsgame.utils

object Constants {

    //1st API
    //const val BASE_URL = "https://clientes.api.greenborn.com.ar/"
    const val BASE_URL = "https://random-word-api.herokuapp.com/"
    const val BASE_URL_FIREBASE = "https://palabrama-default-rtdb.firebaseio.com/"
    const val EP_4_LETTERS = "4letras"
    const val EP_5_LETTERS = "5letras"
    const val EP_6_LETTERS = "6letras"

    //region Datastore
    const val PREFERENCES_NAME = "wordsGame_preferences"
    const val INSTRUCTIONS_KEY = "instructions_seen"
    const val EASY_GAMES_PLAYED_KEY = "easy_games_played"
    const val NORMAL_GAMES_PLAYED_KEY = "normal_games_played"
    const val HARD_GAMES_PLAYED_KEY = "hard_games_played"
    const val LAST_PLAYED_DATE_KEY = "last_played_date"

    const val CAN_ACCESS_TO_APP_KEY = "can_access_to_app"

    const val COINS_KEY = "coins"
    //endregion

    const val ONE_LETTER_HINT_PRICE = 75
    const val KEYBOARD_HINT_PRICE = 50

    //region Firebase
    const val REMOTE_CONFIG_MIN_VERSION_KEY = "min_version"
    //endregion

    //region Game
    const val EASY_WORD_LENGTH = 4
    const val NORMAL_WORD_LENGTH = 5
    const val HARD_WORD_LENGTH = 6

    const val NUMBER_OF_GAMES_ALLOWED = 5
    //endregion

}