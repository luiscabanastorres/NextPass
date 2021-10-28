package com.nextpass.nextiati.nextpass.errorparser

interface ErrorParser {

    fun parse(ex: Exception?): Pair<String, String>



}
