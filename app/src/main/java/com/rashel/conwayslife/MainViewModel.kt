package com.rashel.conwayslife

import android.R.*
import android.R.attr.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {


    var isRunning = false

    private val _uiState = MutableStateFlow(false)

    val uiState: StateFlow<Boolean> = _uiState


    private val _uiGeneration = MutableLiveData<ArrayList<ArrayList<Person>>>()
    val uiGeneration: LiveData<ArrayList<ArrayList<Person>>> = _uiGeneration


    fun checkLivingCondition(myGridArray: ArrayList<ArrayList<Person>>) {

        viewModelScope.launch {

            val newGridArray: ArrayList<ArrayList<Person>> = arrayListOf()

            for (i in 0 until myGridArray.size) {
                val items = arrayListOf<Person>()
                for (j in 0 until myGridArray.size) {
                    val person: Person = myGridArray[i][j].copy()
                    val adjacent = findAdjacent(myGridArray, i, j, 9)
                    if (findIfPersonLivesorDie(adjacent)) {
                        if (!person.isAlive) {
                            person.apply {
                                isAlive = true
                            }
                            Log.d("checkLivingCondition", "changed $i $j")
                            _uiState.value = true
                        }

                    } else {
                        if (person.isAlive) {
                            person.apply {
                                isAlive = false
                            }
                            Log.d("checkLivingCondition", "changed2 $i $j")
                            _uiState.value = true
                        }

                    }

                    items.add(person)
                }
                newGridArray.add(items)
            }

            Log.d("checkLivingCondition", "${Gson().toJson(myGridArray)}")
            Log.d("checkLivingConditionNew", "${Gson().toJson(newGridArray)}")
            delay(500)
            if (isRunning)
                _uiGeneration.value = newGridArray
        }


    }

    // returns true if person live
    // returns false if person die
    private fun findIfPersonLivesorDie(adjacents: ArrayList<Person>): Boolean {
        // any live cell with tow or three live neighbors live
        val livingCount = findNumberOfLivingAdjacent(adjacents)
        if (livingCount == 2 || livingCount == 3) {
            return true

        }
        return false
    }

    private fun findNumberOfLivingAdjacent(adjacents: ArrayList<Person>): Int {
        var count = 0
        adjacents.map {
            if (it.isAlive) count++
        }

        return count
    }

    // find neighbors

    val dx = arrayListOf(-1, 1, 0, 0, -1, -1, 1, 1)
    val dy = arrayListOf(0, 0, -1, 1, -1, 1, -1, 1)

    private fun findAdjacent(
        myGridArray: ArrayList<ArrayList<Person>>,
        xPos: Int,
        yPos: Int,
        max: Int
    ): ArrayList<Person> {

        val adjacent = arrayListOf<Person>()
        for (i in 0 until dx.size) {

            val mX = xPos + dx[i]
            val mY = yPos + dy[i]
            if (mX >= 0 && mX <= max && mY >= 0 && mY <= max) {
                adjacent.add(myGridArray[mX][mY])
            }


        }

        return adjacent
    }


    fun getMatrix(result: (ArrayList<ArrayList<Person>>) -> (Unit)) {

        viewModelScope.launch(Dispatchers.Default) {

            val myGridArray: ArrayList<ArrayList<Person>> = arrayListOf()

            for (i in 1..10) {
                val items = arrayListOf<Person>()
                val line = (i - 1) * 10
                for (j in 1..10) {
                    Log.d("MainActivity", "${line + j}")
                    val item = Person(
                        id = line + j,
                        isAlive = false
                    )
                    items.add(item)
                }
                myGridArray.add(items)
            }

            result(myGridArray)

        }

    }

    fun getMatrixList(myGridArray: ArrayList<ArrayList<Person>>): ArrayList<Person> {
        val myList = arrayListOf<Person>()

        for (i in 0 until myGridArray.size) {
            for (j in 0 until myGridArray.size) {
                myList.add(myGridArray[i][j])
            }
        }
        return myList
    }


}